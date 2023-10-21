package com.wavesignal.mvpdemo;

public class Presenter implements Contract.Presenter, Contract.Model.OnFinishedListener {

    // creating object of View Interface
    private Contract.View mainView;

    // creating object of Model Interface
    private Contract.Model model;

    // instantiating the objects of View and Model Interface
    public Presenter(Contract.View mainView, Contract.Model model) {
        this.mainView = mainView;
        this.model = model;
    }

    @Override
    // operations to be performed
    // on button click
    public void onButtonClick() {
        if (mainView != null) {
            mainView.showProgress();
        }
        model.getNextCourse(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    // method to return the string
    // which will be displayed in the
    // Course Detail TextView
    public void onFinished(String string) {
        if (mainView != null) {
            mainView.setString(string);
            mainView.hideProgress();
        }
    }
}
