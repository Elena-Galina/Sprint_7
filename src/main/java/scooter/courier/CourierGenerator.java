package scooter.courier;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    public static Courier randomeCourier() {
         return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(6))
                .setPassword(RandomStringUtils.randomNumeric(8))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }
}
