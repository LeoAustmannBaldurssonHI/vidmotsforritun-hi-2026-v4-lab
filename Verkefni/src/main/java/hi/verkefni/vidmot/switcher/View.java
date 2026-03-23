package hi.verkefni.vidmot.switcher;
/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 *
 * viðbætur fyrir Ferdaplan verkefni
 */
public enum View {
    MAIN("/verkefni/vidmot/main-view.fxml"),
    VIEWTRIP("/hi/verkefni/vidmot/view-trip.fxml"),
    EDIT("/hi/verkefni/vidmot/edit-trip.fxml");

    private String fileName;

    /**
     * Forritið breytir string "fileName" í einhvern nýr
      * @param fileName
     */
    View(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Til að geta noti fjölmarga fxml skrá, við verðum að geta skilið inn skrá nafnið
     * @return skrá nafn fyrir FXML
     */
    public String getFileName() {
        return fileName;
    }
}
