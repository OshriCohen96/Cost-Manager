/**
 * Oshri Cohen -313295792
 * Aviv Refael-313293805
 */


package com.ac.hit.costmanager;
import com.ac.hit.costmanager.model.CostManagerException;
import com.ac.hit.costmanager.model.IModel;
import com.ac.hit.costmanager.view.View;
import com.ac.hit.costmanager.viewmodel.IViewModel;
import com.ac.hit.costmanager.viewmodel.ViewModel;
import com.ac.hit.costmanager.model.DerbyDBModel;
import com.ac.hit.costmanager.view.IView;


class Application {
    public static void main(String args[]) throws CostManagerException {
        //creating the application components

        IModel model = null;
        try {
            model = new DerbyDBModel();
        } catch (CostManagerException e) {
            e.printStackTrace();
        }
        IView view = (IView) new View();
        IViewModel vm = new ViewModel();

        //connecting the components with each other
        view.setViewModel(vm);
        vm.setModel(model);
        vm.setView(view);
    }

    }
