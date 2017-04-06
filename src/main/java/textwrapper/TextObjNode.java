package textwrapper;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class TextObjNode extends StackPane {

    private Text text;

    public TextObjNode(String text) {
        getChildren().add(this.text = new Text(text));
    }

    public Text getText() {
        return text;
    }

}
