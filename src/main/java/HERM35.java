public class HERM35 {

    public static void main(String[] args) {
        String name = "HERM35";
        System.out.println("Hey! I'm " + name + "!");
        System.out.println("What can I do for you?");
        exit();
    }

    public static void printMessage(String message) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println("-----------------------");
    }

    public static void exit() {
        printMessage("Bye, see you later!");
    }
}
