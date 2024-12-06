package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class DevicesRootImportDto {
    @XmlElement(name = "device")
    private List<DeviceImportDto> devices;

    public DevicesRootImportDto() {}

    public List<DeviceImportDto> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceImportDto> devices) {
        this.devices = devices;
    }
}
