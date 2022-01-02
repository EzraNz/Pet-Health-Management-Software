package view;

public class Main {
    public static void main(String[] args) {
        Main mainApp = new Main();
        try {
            mainApp.loginMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loginMenu() throws Exception {
        AppUI.launch(AppUI.class);
    }
}
