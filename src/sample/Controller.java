package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    public ToggleButton pomodoro;
    @FXML
    ToggleButton shortBreak;
    @FXML
    ToggleButton longBreak;
    @FXML
    ToggleButton soundToggle;
    @FXML
    Label timer;

    private Timeline timeline;

    private int minutes = 25;
    private int seconds = 0;
    private int typeOfTime = 0;
    private String stringSec;
    private boolean timerRunning = false;
    private boolean soundOn = true;
    private AudioInputStream in;
    private Clip clip = AudioSystem.getClip();
    private boolean clipOpen = false;

    public Controller() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){

        setStringSec();
        timer.setText(minutes + ":" + stringSec);

    }
    public void startButtonPressed(){

        if(!timerRunning) {
            countdown();
        }
    }
    public void stopButtonPressed(){

        if(timerRunning){

            timeline.stop();
            timerRunning = false;
        }

    }
    public void resetButtonPressed(){

        if(typeOfTime==0) {
            minutes = 25;
            seconds = 0;
        }
        else if(typeOfTime==1){
            minutes = 5;
            seconds = 0;
        }
        else{
            minutes = 15;
            seconds = 0;
        }
        if(clip.isOpen()) {
            clip.close();
            if(!clip.isOpen())
                System.out.println("clip closed");
        }
        setStringSec();
        timer.setText(minutes + ":" + stringSec);
        timerRunning=false;
    }
    public void pomodoroButtonPressed(){
        if(!timerRunning) {
            pomodoro.setTextFill(Paint.valueOf("#000000"));
            shortBreak.setTextFill(Paint.valueOf("#ffffff"));
            longBreak.setTextFill(Paint.valueOf("#ffffff"));

            this.typeOfTime = 0;
            resetButtonPressed();
            setStringSec();
            if (!this.timerRunning) {
                timer.setText(minutes + ":" + stringSec);
            }
        }
    }
    public void shortBreakButtonPressed(){
        if(!timerRunning) {
            shortBreak.setTextFill(Paint.valueOf("#000000"));
            pomodoro.setTextFill(Paint.valueOf("#ffffff"));
            longBreak.setTextFill(Paint.valueOf("#ffffff"));

            this.typeOfTime = 1;
            resetButtonPressed();
            setStringSec();
            if (!this.timerRunning) {
                timer.setText(minutes + ":" + stringSec);
            }
        }
    }
    public void longBreakButtonPressed(){

        if(!timerRunning) {
            longBreak.setTextFill(Paint.valueOf("#000000"));
            pomodoro.setTextFill(Paint.valueOf("#ffffff"));
            shortBreak.setTextFill(Paint.valueOf("#ffffff"));

            this.typeOfTime = 2;
            resetButtonPressed();
            setStringSec();
            if (!this.timerRunning) {
                timer.setText(minutes + ":" + stringSec);
            }
        }
    }
    public void soundButtonPressed(){

        if(soundOn) {
            soundToggle.setText("Sound: Off");
            soundOn = false;
        }
        else {
            soundToggle.setText("Sound: On");
            soundOn = true;
        }
    }

    public void countdown(){

        this.timerRunning = true;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev ->{

            if(seconds == 0){
                minutes--;
                seconds = 59;
            }
            else{
                seconds--;
            }

            stringSec = String.format("%02d", seconds);
            timer.setText(minutes+":"+stringSec);
            if(minutes+seconds==0 && soundOn){
                try {
                    playSound(in);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }

        }));
        timeline.setCycleCount((minutes*60)+seconds);
        timeline.play();

    }
    public void playSound(AudioInputStream in) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        System.out.println("hi");
        in = AudioSystem.getAudioInputStream(new File("./src/music.wav"));
        try {
            clip.open(in);
            if(clip.isOpen())
                System.out.println("clip open");
            clip.loop(1000);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getTypeOfTime() {
        return typeOfTime;
    }

    public void setTypeOfTime(int typeOfTime) {
        this.typeOfTime = typeOfTime;
    }

    public String getStringSec() {
        return stringSec;
    }

    public void setStringSec() {

        this.stringSec = String.format("%02d", seconds);;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}
