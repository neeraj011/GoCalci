//Project MCC
package project.speech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private String display = "";
    private TextView resultText;
    private TextView resultText1;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText=(TextView) findViewById(R.id.TVresult);
        resultText1=(TextView) findViewById(R.id.TVresult1);
        resultText.setText(display);
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
                        if(resultText1.getText().toString().length()>0)
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                },DELAY);
            }
        });
        tts = new TextToSpeech(this, this);
    }
    private void updateScreen(){
        resultText.setText(display);

    }
    public void onClickNumber(View v){
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

    private void clear(){
        resultText1.setText("");
        resultText.setText(" ");
        display = "";
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
            display=str;

        }
        else if (str.length() <=1 ) {
            resultText.setText("");
            display = "";
        }
    }
    public void onClickEqual(View v){
        String input=resultText.getText().toString();
        evaluate(input);
    }

    public void onButtonClick(View v)
    {
        if (v.getId() == R.id.imageButton)
        {
            promptspeechInput();
        }
    }

    private void promptspeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.UK);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
        try
        {
            startActivityForResult(i,100);
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
            case 100:
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
                    input = input.replaceAll("into","x");
                    input = input.replaceAll("in to","x");
                    input = input.replaceAll("multiplied by","x");
                    input = input.replaceAll("multipliedby","x");
                    input = input.replaceAll("raisedto","^");
                    input = input.replaceAll("raised to","^");
                    input = input.replaceAll("to the power of","^");
                    input = input.replaceAll("to the power","^");
                    resultText.setText(input);
                    evaluate(input);
                }
        }
    }

    private void evaluate(String input)
    {
        Stack<Integer> op  = new Stack<>();
        Stack<Double> val = new Stack<>();
        Stack<Integer> optmp  = new Stack<>();
        Stack<Double> valtmp = new Stack<>();

        input = "0" + input;
        if((input.length()==3 && input.charAt(3)=='%') || input.charAt(input.length())=='%')
        {
            input = input.replaceAll("%","/100");
        }
        else
        {
            input = input.replaceAll("%","/100x");
        }
        String temp = "";
        for (int j = 0;j < input.length();j++)
        {
            char ch = input.charAt(j);
            if (ch != '+' &&  ch!='-' && ch != 'x' && ch != '/' && ch!='^')
            {
                temp = temp + ch;
            }
            else
            {
                try
                {
                    val.push(Double.parseDouble(temp));
                    op.push((int)ch);
                }
                catch(NumberFormatException e)
                {

                }
                temp = "";
            }
        }
        try
        {
            val.push(Double.parseDouble(temp)); //hello
        }
        catch(NumberFormatException e)
        {

        }
        char operators[] = {'^','/','x','+','-'};
        for (int j = 0; j < 5; j++)
        {
            boolean it = false;
            while (!op.isEmpty())
            {
                int optr=0;
                double v1=0.0;
                double v2=0.0;
                try{
                    optr = op.pop();
                    v1 = val.pop();
                    v2 = val.pop();
                }
                catch(EmptyStackException e)
                {

                }
                if (optr == operators[j])
                {
                    if (j==0)
                    {
                        valtmp.push(Math.pow(v2,v1));
                        it = true;
                        break;
                    }
                    else if (j == 1)
                    {
                        valtmp.push(v2 / v1);
                        it = true;
                        break;
                    }
                    else if (j == 2)
                    {
                        valtmp.push(v2 * v1);
                        it = true;
                        break;
                    }
                    else if (j == 3)
                    {
                        valtmp.push(v2 + v1);
                        it = true;
                        break;
                    }
                    else if (j == 4)
                    {
                        valtmp.push(v2 - v1);
                        it = true;
                        break;
                    }
                }
                else
                {
                    valtmp.push(v1);
                    val.push(v2);
                    optmp.push(optr);
                }
            }
            while (!valtmp.isEmpty())
                val.push(valtmp.pop());
            while (!optmp.isEmpty())
                op.push(optmp.pop());
            if (it)
                j--;
        }
        Double ans;
        try
        {
            if(val.isEmpty())
            {
                Toast.makeText(getBaseContext(),"Bad Expression",Toast.LENGTH_SHORT).show();
            }
            else {
                ans = val.pop(); //hello
                String ans1;
                if(ans==Math.floor(ans))
                {
                    ans1= String.valueOf(ans.intValue());
                }
                else
                {
                    ans1 = String.format(Locale.getDefault(),"%.3f", ans);
                }
                resultText1.setText(ans1);
            }
        }
        catch(EmptyStackException e)
        {

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
}