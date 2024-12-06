package softuni.exam.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface SaleService {

    boolean areImported();

    String readSalesFileContent() throws IOException;

    String importSales() throws IOException;
}
