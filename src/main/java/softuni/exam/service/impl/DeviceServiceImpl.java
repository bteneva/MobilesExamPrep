package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.DeviceImportDto;
import softuni.exam.models.dto.DevicesRootImportDto;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.Sale;
import softuni.exam.models.entity.enums.DeviceType;
import softuni.exam.repository.DeviceRepository;
import softuni.exam.repository.SaleRepository;
import softuni.exam.service.DeviceService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final String PATH = "src/main/resources/files/xml/devices.xml";
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final SaleRepository saleRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, SaleRepository saleRepository) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.saleRepository = saleRepository;
    }

    @Override
    public boolean areImported() {
        return this.deviceRepository.count() > 0;
    }

    @Override
    public String readDevicesFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importDevices() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();

        DevicesRootImportDto dtos = this.xmlParser.fromFile(PATH, DevicesRootImportDto.class);
        for (DeviceImportDto dto : dtos.getDevices()) {

            Optional <Device> optionalDeviceByBrandAndModel =
                    this.deviceRepository.findByBrandAndModel(dto.getBrand(), dto.getModel());
            Optional<Sale> optionalDeviceSaleNotExistInDB =
                    this.saleRepository.findById(dto.getSaleId());
            if (!this.validationUtil.isValid(dto)
                    || optionalDeviceByBrandAndModel.isPresent()
                    || optionalDeviceSaleNotExistInDB.isEmpty()) {
                sb.append("Invalid device").append(System.lineSeparator());
                continue;
            }
            Device device = this.modelMapper.map(dto, Device.class);
            device.setDeviceType(DeviceType.valueOf(String.valueOf(dto.getDeviceType())));

            Sale sale = optionalDeviceSaleNotExistInDB.get();
            device.setSale(sale);

            this.deviceRepository.saveAndFlush(device);
            sb.append(String.format("Successfully imported device of type %s with brand %s", dto.getDeviceType(), device.getBrand()))
                    .append(System.lineSeparator());

        }
        return sb.toString();
    }

    @Override
    public String exportDevices() {
        StringBuilder sb = new StringBuilder();
        DeviceType type = DeviceType.SMART_PHONE;
        List<Device> deviceList = deviceRepository.export(type, 1000, 128);
        for (Device device : deviceList) {
            sb.append(String.format("Device brand: %s", device.getBrand())).append(System.lineSeparator())
                    .append(String.format("   *Model: %s", device.getModel())).append(System.lineSeparator())
                    .append(String.format("   **Storage: %d", device.getStorage())).append(System.lineSeparator())
                    .append(String.format("   ***Price: %.2f", device.getPrice())).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
