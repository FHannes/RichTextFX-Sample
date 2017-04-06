package textwrapper;

import org.fxmisc.richtext.model.SegmentOps;

import java.util.Optional;

public class TextObjOps<S> implements SegmentOps<TextObj<S>, S> {

    private final EmptyTextObj<S> emptyTextObj = new EmptyTextObj<>();

    @Override
    public int length(TextObj<S> sTextObj) {
        return sTextObj == emptyTextObj ? 0 : 1;
    }

    @Override
    public char charAt(TextObj<S> sTextObj, int i) {
        return sTextObj == emptyTextObj ? '\0' : '\ufffc';
    }

    @Override
    public String getText(TextObj<S> sTextObj) {
        return sTextObj == emptyTextObj ? "" : "\ufffc";
    }

    @Override
    public TextObj<S> subSequence(TextObj<S> sTextObj, int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Start cannot be negative. Start = " + start);
        }
        if (end > length(sTextObj)) {
            throw new IllegalArgumentException("End cannot be greater than segment's length");
        }
        return start == 0 && end == 1 ? sTextObj : emptyTextObj;
    }

    @Override
    public TextObj<S> subSequence(TextObj<S> sTextObj, int start) {
        if (start < 0) {
            throw new IllegalArgumentException("Start cannot be negative. Start = " + start);
        }
        return start == 0 ? sTextObj : emptyTextObj;
    }

    @Override
    public S getStyle(TextObj<S> sTextObj) {
        return sTextObj.getStyle();
    }

    @Override
    public TextObj<S> setStyle(TextObj<S> sTextObj, S style) {
        return sTextObj == emptyTextObj ? emptyTextObj : sTextObj.setStyle(style);
    }

    @Override
    public Optional<TextObj<S>> join(TextObj<S> currentSeg, TextObj<S> nextSeg) {
        return Optional.empty();
    }

    @Override
    public TextObj<S> createEmpty() {
        return emptyTextObj;
    }

}
