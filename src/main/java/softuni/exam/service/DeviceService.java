package softuni.exam.service;


import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public interface DeviceService {

    boolean areImported();

    String readDevicesFromFile() throws IOException;

	String importDevices() throws IOException, JAXBException;

    String exportDevices();

}
