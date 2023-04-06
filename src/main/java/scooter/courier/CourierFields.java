package scooter.courier;

public class CourierFields {
    private String login;
    private String password;
    public static String incorrectSymbol = "aaa";

    public CourierFields(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierFields fieldsFrom(Courier courier) {
        return new CourierFields(courier.getLogin(), courier.getPassword());
    }
    public static CourierFields loginFieldFrom(Courier courier) {
        return new CourierFields(courier.getLogin(),"");
    }
    public static CourierFields passwordFieldFrom(Courier courier) {
        return new CourierFields("", courier.getPassword());
    }
    public static CourierFields invalidLoginFieldFrom(Courier courier) {
        return new CourierFields(courier.getLogin() + incorrectSymbol,courier.getPassword());
    }
    public static CourierFields invalidPasswordFieldFrom(Courier courier) {
        return new CourierFields(courier.getLogin(),courier.getPassword() + incorrectSymbol);
    }

}
