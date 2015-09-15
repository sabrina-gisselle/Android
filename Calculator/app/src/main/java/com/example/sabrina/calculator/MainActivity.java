package com.example.sabrina.calculator;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    static final private String CURRENT_ENTRY = "storedEntry";

    private TextView mTextViewRepresentation = null;
    private ArrayList<String> mInputs = new ArrayList<>();
    private String mEntry = "";
    private boolean mIsOperator;
    private boolean mIsPrevDominantOperator = false;
    private String mDominantOperator;
    private boolean mIsEquals = false;
    private boolean mErrorThrown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRepresentation = (TextView) findViewById(R.id.textView);

        mEntry = getPreferences(MODE_PRIVATE ).getString(CURRENT_ENTRY, "");
        mTextViewRepresentation.setText(mEntry);
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(CURRENT_ENTRY, mEntry);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        try{
            switch (view.getId()) {

                case R.id.btn0:
                    onNumberClick(R.string.btn0_text);
                    break;

                case R.id.btn1:
                    onNumberClick(R.string.btn1_text);
                    break;

                case R.id.btn2:
                    onNumberClick(R.string.btn2_text);
                    break;

                case R.id.btn3:
                    onNumberClick(R.string.btn3_text);
                    break;

                case R.id.btn4:
                    onNumberClick(R.string.btn4_text);
                    break;

                case R.id.btn5:
                    onNumberClick(R.string.btn5_text);
                    break;

                case R.id.btn6:
                    onNumberClick(R.string.btn6_text);
                    break;

                case R.id.btn7:
                    onNumberClick(R.string.btn7_text);
                    break;

                case R.id.btn8:
                    onNumberClick(R.string.btn8_text);
                    break;

                case R.id.btn9:
                    onNumberClick(R.string.btn9_text);
                    break;

                case R.id.btn_dot:
                    if(mEntry.isEmpty() || (!mEntry.isEmpty() && mEntry.charAt(mEntry.length()-1) != '.')){
                        onNumberClick(R.string.btn_dot_text);
                    }
                    break;
                case R.id.btn_divide:
                    onDomOperators(R.string.btn_divide_text);
                    break;

                case R.id.btnX:
                    onDomOperators(R.string.btnX_text);
                    break;

                case R.id.btn_minus:
                    onNonDomOperators(R.string.btn_minus_text);
                    break;

                case R.id.btn_plus:
                    onNonDomOperators(R.string.btn_plus_text);
                    break;

                case R.id.btn_signs:
                    if (mEntry == ""){
                        mTextViewRepresentation.setText(mTextViewRepresentation.getText() + "-");
                        mEntry = "-";
                    } else if (mEntry == "-"){
                        mTextViewRepresentation.setText(mTextViewRepresentation.getText().subSequence(0, mTextViewRepresentation.length()-1));
                        mEntry = "";
                    }
                    break;

                case R.id.btn_clear:
                    clearAll();
                    break;

                case R.id.btn_equals:
                    if(mErrorThrown){
                        throw new Exception();
                    }
                    else if( (mTextViewRepresentation.getText().length() > 0 || mEntry != null) && !mIsOperator) {
                        if( mIsPrevDominantOperator ) {
                            if (mDominantOperator.equals(getResources().getString(R.string.btn_divide_text))) {
                                divide();
                            } else {
                                multiply();
                            }
                        }else{
                            mInputs.add(mEntry);
                        }
                        if(mErrorThrown){
                            throw new Exception();
                        } else {
                            CharSequence total = calculate();
                            String text = mTextViewRepresentation.getText() + " = " + total;
                            clearAll();
                            mEntry = (String) total;
                            mTextViewRepresentation.setText(text);
                            mIsEquals = true;
                        }
                    }
                    break;
            }
        }catch(Exception ex){
            mErrorThrown = true;
            mTextViewRepresentation.setText(mTextViewRepresentation.getText() + "= Error");
        }
    }

    private void operatorAfterEquals(){
        if(mIsEquals){
            mTextViewRepresentation.setText(mEntry);
        }
    }

    private void onDomOperators(int btn_text){
        if( mTextViewRepresentation.getText().length() > 0 && !mIsOperator && !mErrorThrown) {
            operatorAfterEquals();
            if( mIsPrevDominantOperator ) {
                if (mDominantOperator.equals(getResources().getString(R.string.btn_divide_text))) {
                    divide();
                } else {
                    multiply();
                }
            }else{
                mInputs.add(mEntry);
            }
            mDominantOperator = getResources().getString(btn_text);

            mEntry = "";
            mTextViewRepresentation.setText(mTextViewRepresentation.getText() + getResources().getString(btn_text));
            mIsOperator = true;
            mIsPrevDominantOperator =true;
            mIsEquals=false;
        }
    }
    private void onNonDomOperators(int btn_text) {
        if( mTextViewRepresentation.getText().length() > 0 && !mIsOperator && !mErrorThrown) {
            operatorAfterEquals();
            addToList(getResources().getString(btn_text));
            mTextViewRepresentation.setText(mTextViewRepresentation.getText() + getResources().getString(btn_text));
            mIsOperator = true;
            mIsPrevDominantOperator =false;
            mIsEquals=false;
            mErrorThrown = false;
        }
    }

    private void clearAll(){
        mInputs.clear();
        mEntry="";
        mTextViewRepresentation.setText("");
        mIsOperator = false;
        mIsPrevDominantOperator = false;
        mDominantOperator = "";
        mIsEquals = false;
        mErrorThrown = false;
    }
    private void onNumberClick(int btn_text) {
        if(mIsEquals || mErrorThrown){
            clearAll();
        }
        mIsOperator = false;
        mEntry += getResources().getString(btn_text);
        mTextViewRepresentation.setText(mTextViewRepresentation.getText()=="" ? getResources().getString(btn_text) : mTextViewRepresentation.getText() + getResources().getString(btn_text));
        mIsEquals = false;
        mErrorThrown = false;
    }

    private CharSequence calculate() {
        if(mTextViewRepresentation.getText().length() == 1){
            return mEntry;
        }
        if(mInputs.size() == 1){
            return mInputs.get(0);
        }
        double firstElement, secondElement;
        CharSequence operator;
        int index = 0;
        firstElement = Double.valueOf(mInputs.get(index++));
        operator = mInputs.get(index++);
        secondElement = Double.valueOf(mInputs.get(index++));
        if(operator.equals(getResources().getString(R.string.btn_plus_text))){
            firstElement += secondElement;
        }else{
            firstElement -= secondElement;
        }
        for(; index < mInputs.size()-1; index+=2){
            operator = mInputs.get(index);
            secondElement = Double.valueOf(mInputs.get(index+1));
            if(operator.equals(getResources().getString(R.string.btn_plus_text))){
                firstElement += secondElement;
            }else{
                firstElement -= secondElement;
            }
        }
        return String.valueOf(firstElement);
    }

    private void addToList(String operator){
        if(mIsPrevDominantOperator){
            if(mDominantOperator.equals(getResources().getString(R.string.btn_divide_text))){
                divide();
            }else{
                multiply();
            }
        }else{
            mInputs.add(mEntry);
        }
        mEntry = "";
        mInputs.add(operator);
    }

    private void divide(){
        int length = mInputs.size()-1;
        if(Double.valueOf(mEntry) == 0.0) {
            mErrorThrown = true;
            return;
        }
        mInputs.set(length, Double.toString((Double.valueOf(mInputs.get(length)) / Double.valueOf(mEntry))));
    }

    private void multiply(){
        int length = mInputs.size()-1;
        mInputs.set(length, Double.toString((Double.valueOf(mInputs.get(length))*Double.valueOf(mEntry))));
    }
}
