package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerImportDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;

import javax.annotation.processing.Generated;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private static final String PATH = "src/main/resources/files/json/sellers.json";

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importSellers() throws IOException {
        StringBuilder sb = new StringBuilder();

        SellerImportDto[] dtos = gson.fromJson(readSellersFromFile(), SellerImportDto[].class);
        for (SellerImportDto dto : dtos) {
           Optional<Seller> optionalSellerByLastName =this.sellerRepository.findSellerByLastName(dto.getLastName());
           if (optionalSellerByLastName.isPresent() || !this.validationUtil.isValid(dto)) {
               sb.append("Invalid seller").append(System.lineSeparator());
               continue;
           }
            Seller seller = this.modelMapper.map(dto, Seller.class);
           this.sellerRepository.saveAndFlush(seller);
           sb.append(String.format("Successfully imported seller %s %s", dto.getFirstName(), dto.getLastName()));
           sb.append(System.lineSeparator());
        }


        return sb.toString();
    }
}
