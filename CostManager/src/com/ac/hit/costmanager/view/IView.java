

package com.ac.hit.costmanager.view;
import com.ac.hit.costmanager.viewmodel.IViewModel;
/**
 * IView interface is take care of the UI
 */
public interface IView
{
    //setViewModel method set the input ViewModel as local variable for View class
    public void setViewModel(IViewModel vm);
    //showMessage method display the input String on the message screen in the application
    public void showMessage(String text);
}
