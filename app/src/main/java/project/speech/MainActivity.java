package project.speech;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;
import java.security.Policy;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,View.OnClickListener {
    private String display = "";
    private String currentOperator = "";
    private String result = "";
    private TextView resultText;
    private TextView resultText1;
    TextToSpeech tts;
    private String lastResultObtain = "";
    private String currentDisplayedInput = "";
    private String inputToBeParsed = "";
    private project.speech.Calculator mCalculator;
    private static String PREFS_NAME = "memory";
    private Button button0, button1, button2,button3,button4,button5,button6,button7,button8,button9, buttonDivide,buttonMultiply,buttonSubtract,buttonAdd, buttonPercentage,  buttonDecimal, closeParenthesis, openParenthesis, buttonpi;
    private Button buttonSin, buttonLn,buttonCos, buttonLog, buttonTan, buttonexp, buttonYPowerX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCalculator = new project.speech.Calculator();
        resultText=(TextView) findViewById(R.id.TVresult);
        resultText1=(TextView) findViewById(R.id.TVresult1);
        resultText.setText(display);
        button0 = (Button)findViewById(R.id.btn0);
        button1 = (Button)findViewById(R.id.btn1);
        button2 = (Button)findViewById(R.id.btn2);
        button3 = (Button)findViewById(R.id.btn3);
        button4 = (Button)findViewById(R.id.btn4);
        button5 = (Button)findViewById(R.id.btn5);
        button6 = (Button)findViewById(R.id.btn6);
        button7 = (Button)findViewById(R.id.btn7);
        button8 = (Button)findViewById(R.id.btn8);
        button9 = (Button)findViewById(R.id.btn9);
        buttonDivide = (Button)findViewById(R.id.btnDiv);
        buttonMultiply = (Button)findViewById(R.id.btnMul);
        buttonSubtract = (Button)findViewById(R.id.btnMinus);
        buttonAdd = (Button)findViewById(R.id.btnPlus);
        buttonPercentage = (Button)findViewById(R.id.btnPer);
        buttonDecimal = (Button)findViewById(R.id.btnDot);
        closeParenthesis = (Button)findViewById(R.id.btnCbrc);
        openParenthesis = (Button)findViewById(R.id.btnObrc);
        buttonexp = (Button)findViewById(R.id.btnexp);
        buttonYPowerX = (Button)findViewById(R.id.btnPow);
        buttonSin = (Button)findViewById(R.id.btnsin);
        buttonCos = (Button)findViewById(R.id.btncos);
        buttonTan = (Button)findViewById(R.id.btntan);
        buttonLn = (Button)findViewById(R.id.btnLn);
        buttonLog = (Button)findViewById(R.id.btnLog);
        buttonpi = (Button)findViewById(R.id.btnpi);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonPercentage.setOnClickListener(this);
        buttonDecimal.setOnClickListener(this);
        closeParenthesis.setOnClickListener(this);
        openParenthesis.setOnClickListener(this);
        buttonpi.setOnClickListener(this);
        buttonexp.setOnClickListener(this);
        buttonYPowerX.setOnClickListener(this);
        buttonSin.setOnClickListener(this);
        buttonCos.setOnClickListener(this);
        buttonTan.setOnClickListener(this);
        buttonLn.setOnClickListener(this);
        buttonLog.setOnClickListener(this);
        resultText1.addTextChangedListener(new TextWatcher()
        {
            boolean isTyping=false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
            private Timer timer=new Timer();
            private final long DELAY=50;
            @Override
            public void afterTextChanged(Editable s)
            {
                Log.d("","");
                if(!isTyping)
                {
                    Log.d("","Started Typing");
                    isTyping=true;
                }
                timer.cancel();
                timer=new Timer();
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        isTyping=false;
                        String res=resultText1.getText().toString();
                        String text = "The answer is "+ res;
                        if(resultText1.getText().toString().length()>0) {
                            if(res.equals("Error")){
                                tts.speak(res, TextToSpeech.QUEUE_FLUSH, null);
                            }
                            else
                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                },DELAY);
            }
        });
        tts = new TextToSpeech(this, this);
    }

    private void obtainInputValues(String input){
        switch (input){
            case "0":
                currentDisplayedInput += "0";
                inputToBeParsed += "0";
                break;
            case "1":

                currentDisplayedInput += "1";
                inputToBeParsed += "1";

                break;
            case "2":

                currentDisplayedInput += "2";
                inputToBeParsed += "2";
                break;
            case "3":

                currentDisplayedInput += "3";
                inputToBeParsed += "3";
                break;
            case "4":

                currentDisplayedInput += "4";
                inputToBeParsed += "4";

                break;
            case "5":

                currentDisplayedInput += "5";
                inputToBeParsed += "5";
                break;

            case "6":

                currentDisplayedInput += "6";
                inputToBeParsed += "6";

                break;
            case "7":
                currentDisplayedInput += "7";
                inputToBeParsed += "7";
                break;
            case "8":
                currentDisplayedInput += "8";
                inputToBeParsed += "8";
                break;
            case "9":
                currentDisplayedInput += "9";
                inputToBeParsed += "9";
                break;
            case ".":
                currentDisplayedInput += ".";
                inputToBeParsed += ".";
                break;
            case "+":
                currentDisplayedInput += "+";
                inputToBeParsed += "+";
                break;
            case "-":
                currentDisplayedInput += "-";
                inputToBeParsed += "-";
                break;
            case "/":
                currentDisplayedInput += "/";
                inputToBeParsed += "/";
                break;
            case "X":
                currentDisplayedInput += "*";
                inputToBeParsed += "*";
                break;
            case "(":
                currentDisplayedInput += "(";
                inputToBeParsed += "(";
                break;
            case ")":
                currentDisplayedInput += ")";
                inputToBeParsed += ")";
                break;
            case "%":

                currentDisplayedInput += "%";
                inputToBeParsed += "%";

                break;
            case "ln":

                currentDisplayedInput += "ln(";
                inputToBeParsed += "ln(";

                break;
            case "lg":

                currentDisplayedInput += "log(";
                inputToBeParsed += "log(";

                break;
            case "e":
                currentDisplayedInput += "e";
                inputToBeParsed += "e";
                break;
            case "^":
                currentDisplayedInput += "^";
                inputToBeParsed += "^";
                break;
            case "sin":

                currentDisplayedInput += "sin(";
                inputToBeParsed += "sin(";

                break;
            case "cos":

                currentDisplayedInput += "cos(";
                inputToBeParsed += "cos(";

                break;
            case "tan":

                currentDisplayedInput += "tan(";
                inputToBeParsed += "tan(";

                break;
            case "π":
                currentDisplayedInput += "pi";
                inputToBeParsed += "pi";
                break;
        }
        resultText.setText(currentDisplayedInput);
    }
    private String removeTrailingZero(String formattingInput){
        if(!formattingInput.contains(".")){
            return formattingInput;
        }
        int dotPosition = formattingInput.indexOf(".");
        String newValue = formattingInput.substring(dotPosition, formattingInput.length());
        if(newValue.equals(".0")){
            return formattingInput.substring(0, dotPosition);
        }
        return formattingInput;
    }


    private double isANumber(String numberInput){
        double result = Double.NaN;
        try{
            result = Double.parseDouble(numberInput);
        }catch(NumberFormatException nfe){
        }
        return result;
    }
    private void addToMemoryStorage(Context context, double inputToStore){
        float returnPrefValue = getPreference(context);
        float newValue = returnPrefValue + (float)inputToStore;
        setPreference(context, newValue);
    }
    private void subtractMemoryStorage(Context context, double inputToStore){
        float returnPrefValue = getPreference(context);
        float newValue = returnPrefValue - (float)inputToStore;
        setPreference(context, newValue);
    }
    private void clearMemoryStorage(Context context){
        setPreference(context, 0);
    }
    private String getStoredPreferenceValue(Context context){
        float returnedValue = getPreference(context);
        return String.valueOf(returnedValue);
    }
    static public boolean setPreference(Context c, float value) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("key", value);
        return editor.commit();
    }
    static public float getPreference(Context c) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        float value = settings.getFloat("key", 0);
        return value;
    }






    private void clear(){
        resultText1.setText("");
        resultText.setText(" ");
        currentDisplayedInput = "";
        inputToBeParsed = "";
    }
    public void onClickClear(View v){

        clear();
    }
    public void onClickBack(View v){

        String str=resultText.getText().toString();
        if(resultText1.getText().toString().length()>0){resultText1.setText("");}
        if (str.length() >1 ) {
            str = str.substring(0, str.length() - 1);
            resultText.setText(str);
            currentDisplayedInput =str;
            inputToBeParsed = str;
        }
        else if (str.length() <=1 ) {
            resultText.setText("");
            currentDisplayedInput = "";
            inputToBeParsed = "";
        }
    }

    public void onClickEqual(View v){
        String enteredInput = resultText.getText().toString();
        // call a function that will return the result of the calculate.
        String resultObject = mCalculator.getResult(currentDisplayedInput, inputToBeParsed);
        resultText1.setText(removeTrailingZero(resultObject));
    }

    public void onButtonClick(View v)
    {
        if (v.getId() == R.id.imageButton) ;
        {
            promptspeechInput();
        }
    }

    private void promptspeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.UNICODE_LOCALE_EXTENSION);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
        try
        {
            startActivityForResult(i,50);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(MainActivity.this,"Sorry speech not supported",Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_Code,int result_code, Intent i )
    {
        super.onActivityResult(request_Code,result_code,i);

        switch (request_Code)
        {
            case 50:
                if(result_code== RESULT_OK && i!=null)
                {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String input=result.get(0);
                    input = input.replaceAll("plus","+");
                    input = input.replaceAll("minus","-");
                    input = input.replaceAll("dividedby","/");
                    input = input.replaceAll("divided by","/");
                    input = input.replaceAll("divideby","/");
                    input = input.replaceAll("divide by","/");
                    input = input.replaceAll("bye","/");
                    input = input.replaceAll("by","/");
                    input = input.replaceAll("into","*");
                    input = input.replaceAll("in to","*");
                    input = input.replaceAll("multiplied by","*");
                    input = input.replaceAll("multipliedby","*");
                    input = input.replaceAll("x","*");
                    input = input.replaceAll("X","*");
                    input = input.replaceAll("raisedto","^");
                    input = input.replaceAll("raised to","^");
                    input = input.replaceAll("to the power of","^");
                    input = input.replaceAll("to the power","^");
                    input = input.replaceAll("cos","cos(");
                    input = input.replaceAll("cos of","cos(");
                    input = input.replaceAll("cosine of","cos(");
                    input = input.replaceAll("cosine","cos(");
                    input = input.replaceAll("sine","sin(");
                    input = input.replaceAll("sine of","sin(");
                    input = input.replaceAll("sin","sin(");
                    input = input.replaceAll("sin of","sin(");
                    input = input.replaceAll("tangent","tan(");
                    input = input.replaceAll("tan","tan(");
                    input = input.replaceAll("tan of","tan(");
                    input = input.replaceAll("log of","log(");
                    input = input.replaceAll("log","log(");
                    input = input.replaceAll("natural log","ln(");
                    input = input.replaceAll("natural log of","ln(");
                    input = input.replaceAll("percentage","%");
                    input = input.replaceAll("percent of","%");
                    input = input.replaceAll("percentage of","%");
                    input = input.replaceAll("modulo","%");
                    input = input.replaceAll("modulus","%");
                    input = input.replaceAll("natural log of","ln");
                    input = input.replaceAll("open Parentheses","(");
                    input = input.replaceAll("open bracket","(");
                    input = input.replaceAll("close parentheses",")");
                    input = input.replaceAll("close bracket",")");
                    input = input.replaceAll("e","e");
                    input = input.replaceAll("exponential","e");
                    input = input.replaceAll("pi","pi");
                    input = input.replaceAll("pie","pi");
                    input = input.replaceAll("py","pi");
                    input = input.replaceAll("point",".");
                    input = input.replaceAll("dot",".");
                    resultText.setText(input);
                    String resultObject = mCalculator.getResult(input, input);
                    resultText1.setText(removeTrailingZero(resultObject));

                }
        }
    }

    @Override
    public void onInit(int status)
    {
        if (status== TextToSpeech.SUCCESS)
        {

            Locale bahasa = tts.getLanguage();
            int result =tts.setLanguage(bahasa);
            if (result== TextToSpeech.LANG_MISSING_DATA || result== TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","this language is not supported");
            }
            else
            {
            }
        }
        else
        {
            Log.e("TTS","initialization failed");
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String data = button.getText().toString();
        obtainInputValues(data);
    }
}
