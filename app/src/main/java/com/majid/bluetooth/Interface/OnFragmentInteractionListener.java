package com.majid.bluetooth.Interface;

import com.majid.bluetooth.Model.DeviceItem;

/**
 * Created by majid on 2/9/17.
 */
public interface OnFragmentInteractionListener {

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void onFragmentInteraction(DeviceItem device);
}
