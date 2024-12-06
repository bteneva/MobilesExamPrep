package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entity.enums.DeviceType;

import javax.persistence.Enumerated;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceImportDto {
//     <device>
//        <brand>HTC</brand>
//        <device_type>SMART_PHONE</device_type>
//        <model>Ultra23+</model>
//        <price>999.00</price>
//        <storage>128</storage>
//        <sale_id>1</sale_id>
//    </device>
    @XmlElement(name ="brand")
    @Length(min= 2, max = 20)
    private String brand;
    @XmlElement(name = "device_type")
    @Enumerated
    private DeviceType deviceType;
    @XmlElement(name = "model")
    @Length(min = 1, max = 20)
    private String model;
    @XmlElement(name = "price")
    @PositiveOrZero
    private double price;
    @XmlElement(name = "storage")
    @PositiveOrZero
    private int storage;
    @XmlElement(name = "sale_id")
    private long saleId;

    public DeviceImportDto() {
    }

    public @Length(min = 2, max = 20) String getBrand() {
        return brand;
    }

    public void setBrand(@Length(min = 2, max = 20) String brand) {
        this.brand = brand;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public @Length(min = 1, max = 20) String getModel() {
        return model;
    }

    public void setModel(@Length(min = 1, max = 20) String model) {
        this.model = model;
    }

    @PositiveOrZero
    public double getPrice() {
        return price;
    }

    public void setPrice(@PositiveOrZero double price) {
        this.price = price;
    }

    @PositiveOrZero
    public int getStorage() {
        return storage;
    }

    public void setStorage(@PositiveOrZero int storage) {
        this.storage = storage;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }
}
