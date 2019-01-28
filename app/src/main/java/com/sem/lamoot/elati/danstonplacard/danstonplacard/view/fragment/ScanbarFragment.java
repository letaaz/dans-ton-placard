package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils.FetchData;

import java.util.concurrent.ExecutionException;

import es.dmoral.toasty.Toasty;

public class ScanbarFragment extends Fragment {
    private int MY_PERMISSIONS_REQUEST_CAMERA;
    private CodeScanner mCodeScanner;
    private Context mContext;
    private int idLDC;
    private String mPiece;

    public static Fragment newInstance(String mPiece, int idLDC) {
        Bundle args = new Bundle();
        args.putString("mPiece", mPiece);
        args.putInt("idLDC", idLDC);
        ScanbarFragment scanbarFragment = new ScanbarFragment();
        scanbarFragment.setArguments(args);
        return scanbarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mPiece = getArguments().getString("mPiece");
            idLDC = getArguments().getInt("idLDC");
        }
        checkCameraPermissions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanbar, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);

        // Set the Scanner bar code
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);

        // Set method called when barcode is decoded
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            //Toasty.success(mContext, "Produit récupéré : " + result.getText(), Toast.LENGTH_SHORT, true).show();
            mCodeScanner.releaseResources();
            final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.beepsound3);
            mp.start();
            getProduct(result.getText());
        }));

        // Set method called when scanner barcode can't be load
        mCodeScanner.setErrorCallback(error -> activity.runOnUiThread(() -> Toasty.error(mContext, "La caméra n'a pas pu être initialisée.", Toast.LENGTH_SHORT, true).show()));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * Get product from API and add it to database
     * @param text Barcode of the product (in text format)
     */
    private void getProduct(String text) {
        String data_product = "";
        try {
            if(idLDC != -1) { // Add product in LDC
                data_product = new FetchData(getActivity().getApplicationContext(), text, "DIVERS", idLDC).execute(text).get();
            }
            else { // Add product in inventory
                data_product = new FetchData(getActivity().getApplicationContext(), text,  mPiece, -1).execute(text).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        mCodeScanner.startPreview();
    }

    /**
     * Method called to verify that the user has given the permissions to use on the camera
     */
    private void checkCameraPermissions() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }
}