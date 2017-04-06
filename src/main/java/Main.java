import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.StyledText;
import org.fxmisc.richtext.model.TextOps;
import org.reactfx.util.Either;
import textwrapper.TextObj;
import textwrapper.TextObjInst;
import textwrapper.TextObjOps;

import java.util.function.BiConsumer;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private TextOps<StyledText<TextStyle>, TextStyle> styledTextOps;

    private Node createNode(Either<StyledText<TextStyle>, TextObj<TextStyle>> seg,
                            BiConsumer<? super TextExt, TextStyle> applyStyle) {
        if (seg.isLeft()) {
            return StyledTextArea.createStyledTextNode(seg.getLeft(), styledTextOps, applyStyle);
        } else {
            return seg.getRight().createNode();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Embedded Text Sample");

        styledTextOps = StyledText.textOps();
        TextObjOps<TextStyle> valueRefOps = new TextObjOps<>();
        GenericStyledArea<ParagraphStyle, Either<StyledText<TextStyle>, TextObj<TextStyle>>, TextStyle> textArea =
                new GenericStyledArea<>(
                        ParagraphStyle.EMPTY,
                        (paragraph, style) -> paragraph.setStyle(style.toCss()),
                        TextStyle.EMPTY.updateFontSize(12).updateFontFamily("Serif").updateTextColor(Color.BLACK),
                        styledTextOps._or(valueRefOps),
                        seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss()))
                );
        textArea.setWrapText(true);
        textArea.setStyleCodecs(
                ParagraphStyle.CODEC,
                Codec.eitherCodec(StyledText.codec(TextStyle.CODEC), TextObj.codec(TextStyle.CODEC)));
        ReadOnlyStyledDocument<ParagraphStyle, Either<StyledText<TextStyle>, TextObj<TextStyle>>, TextStyle> ros =
                ReadOnlyStyledDocument.fromSegment(Either.right(new TextObjInst<>("TextNode", TextStyle.EMPTY)),
                        ParagraphStyle.EMPTY, TextStyle.EMPTY, textArea.getSegOps());
        textArea.replaceSelection(ros);
        VirtualizedScrollPane<GenericStyledArea<ParagraphStyle, Either<StyledText<TextStyle>, TextObj<TextStyle>>, TextStyle>> vsPane =
                new VirtualizedScrollPane<>(textArea);

        StackPane root = new StackPane();
        root.getChildren().add(textArea);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

}
