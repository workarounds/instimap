package com.mrane.campusmap;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class WordSpan extends ClickableSpan 
{

    private int id;
    private TextPaint textpaint;
    public boolean shouldHilightWord = false;
    public WordSpan(int anID, String txt, int selected) {
        id =anID;
        // if the word selected is the same as the ID set the highlight flag
        if(selected == id)  {
            shouldHilightWord = true;

        }


    }

    @Override
    public void updateDrawState(TextPaint ds) {
        textpaint = ds;
        ds.setColor(ds.linkColor);
        if(shouldHilightWord){
            textpaint.bgColor = Color.GRAY;         
            textpaint.setARGB(255, 255, 255, 255);

        }
        //Remove default underline associated with spans
        ds.setUnderlineText(false);

    }

    public void changeSpanBgColor(View widget){
        shouldHilightWord = true;
        updateDrawState(textpaint);
        widget.invalidate();


    }
    @Override
    public void onClick(View widget) {

        // TODO Auto-generated method stub

    }


    /**
     * This function sets the span to record the word number, as the span ID
     * @param spanID
     */
    public void setSpanTextID(int spanID){
        id = spanID;
    }

    /**
     * Return the wordId of this span
     * @return id
     */
    public int getSpanTextID(){
        return id;
    }
}