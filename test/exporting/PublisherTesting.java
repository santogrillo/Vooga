package exporting;

public class PublisherTesting {

    public static void main(String[] args) {
        try {
            Publisher publisher = new Publisher();
            String fileLink = publisher.uploadFile("application/zip",
                    "data/games/jar-package-testing.jar");
            System.out.println("Share your game with this link: " + fileLink);
        } catch (Exception e) {
            // No good
            e.printStackTrace();
            System.out.println("Failed to publish to your Google Drive account!");
        }
    }
}
