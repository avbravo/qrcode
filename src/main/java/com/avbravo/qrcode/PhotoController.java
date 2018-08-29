/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avbravo.qrcode;

import com.avbravo.avbravoutils.JsfUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import org.primefaces.event.CaptureEvent;

/**
 *
 * @author r
 */
@ManagedBean(name = "photoController")
@SessionScoped
public class PhotoController implements Serializable {

    private String resultText;

    /**
     * Creates a new instance of PhotoController
     */
    public PhotoController() {
    }

    public void oncapture(CaptureEvent captureEvent) {
        try {
            if (captureEvent != null) {
                JsfUtil.successMessage("capturado");
                Result result = null;
                BufferedImage image = null;

                InputStream in = new ByteArrayInputStream(captureEvent.getData());

                image = ImageIO.read(in);

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                result = new MultiFormatReader().decode(bitmap);
                if (result != null) {
                    setResultText(result.getText());
                    System.out.println("---> texto "+result.getText());
                    JsfUtil.successMessage("texto "+result.getText());
                }
            }else{
                JsfUtil.warningMessage("no captado...");
            }
        } catch (NotFoundException ex) {
//             JsfUtil.errorMessage("notFound:" +ex.getLocalizedMessage());
            // fall thru, it means there is no QR code in image
        } catch (ReaderException ex) {
//            JsfUtil.errorMessage("Reader: " +ex.getLocalizedMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
//             JsfUtil.errorMessage("Io: "+ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    /**
     * @return the resultText
     */
    public String getResultText() {
        return resultText;
    }

    /**
     * @param resultText the resultText to set
     */
    public void setResultText(String resultText) {
        this.resultText = resultText;
    }
}
