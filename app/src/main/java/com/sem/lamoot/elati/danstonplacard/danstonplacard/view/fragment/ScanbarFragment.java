package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils.FetchData;

import java.util.concurrent.ExecutionException;

import es.dmoral.toasty.Toasty;

public class ScanbarFragment extends Fragment {
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanbar, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        //mCodeScanner.setFormats(listOf(BarcodeFormat.EAN_13));
        
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toasty.success(mContext, "Produit récupéré : " + result.getText(), Toast.LENGTH_SHORT, true).show();
                        mCodeScanner.releaseResources();
                        final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.beepsound3);
                        mp.start();
                        getProduct(result.getText());
                    }
                });
            }
        });
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@androidx.annotation.NonNull Exception error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(activity.getApplicationContext(), "La caméra n'a pas pu être initialisée.",
//                                Toast.LENGTH_LONG).show();
                        Toasty.error(mContext, "La caméra n'a pas pu être initialisée.", Toast.LENGTH_SHORT, true).show();

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    private void getProduct(String text) {
        String data_product = "";
        try {
            if(idLDC != -1)
            {
                data_product = new FetchData(getActivity().getApplicationContext(), text, "DIVERS", idLDC).execute(text).get();
            }
            else {
                data_product = new FetchData(getActivity().getApplicationContext(), text,  mPiece, -1).execute(text).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        mCodeScanner.startPreview();

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
}