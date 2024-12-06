package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SaleImportDto;
import softuni.exam.models.entity.Sale;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SaleRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SaleService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {
    private static final String PATH = "src/main/resources/files/json/sales.json";

    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final SellerRepository sellerRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, SellerRepository sellerRepository) {
        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return this.saleRepository.count() > 0;
    }

    @Override
    public String readSalesFileContent() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importSales() throws IOException {
        StringBuilder sb = new StringBuilder();
        SaleImportDto[] dtos = gson.fromJson(readSalesFileContent(), SaleImportDto[].class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (SaleImportDto dto : dtos) {
            Optional<Sale> saleOptionalSameNumber =
                    this.saleRepository.findByNumber(dto.getNumber());

            if (saleOptionalSameNumber.isPresent() || !validationUtil.isValid(dto)) {
                sb.append("Invalid sale").append(System.lineSeparator());
                continue;
            }

            // Map DTO to Sale
            Sale sale = this.modelMapper.map(dto, Sale.class);

            // Set saleDate
            sale.setSaleDate(LocalDateTime.parse(dto.getSaleDate(), formatter));

            // Set seller
            Seller seller = this.sellerRepository.findById((long)dto.getSeller())
                    .orElse(null);
            if (seller == null) {
                sb.append("Invalid sale").append(System.lineSeparator());
                continue;
            }
            sale.setSeller(seller);

            // Save Sale
            this.saleRepository.saveAndFlush(sale);
            sb.append(String.format("Successfully imported sale with number %s", dto.getNumber()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

}
