import service.ShopService;

public class Main {
    public static void main(String[] args) {

        ShopService shopService =  ShopService.getInstance();
        try{
            shopService.runStartMenu();
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}