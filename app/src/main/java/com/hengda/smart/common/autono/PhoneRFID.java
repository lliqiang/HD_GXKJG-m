package com.hengda.smart.common.autono;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

@SuppressWarnings("ALL")
public class PhoneRFID {
    private AudioRecord phoneMIC;
    private int recBufferSize;
    private boolean stoped = true;
    private Thread inThread;
    private int autoNum;
    private int temback = 0;
    private int count = 0;
    private int monitorUV1 = 0;
    private int monitorUV2 = 0;

    // --------------------------------------------------------------------------------------
    public void startDec() throws Exception {
        initAudioHardware();
        stoped = false;
        inThread = new Thread(new Runnable() {
            public void run() {
                try {
                    startPhoneMIC();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        inThread.start();
    }

    private void initAudioHardware() throws Exception {
        //recBufferSize = 8192;// AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);
        recBufferSize =  AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT)*5;
        phoneMIC = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, recBufferSize);
    }

    private void startPhoneMIC() throws Exception {
        phoneMIC.startRecording();
        int monitorUV = 0;
        while ((!Thread.interrupted()) && !stoped) {
            byte[] buffer = new byte[recBufferSize];
            int b = phoneMIC.read(buffer, 0, recBufferSize);
            int c, d;
            String aa = "";
            if (monitorUV == 1) {
                c = GetFilterValueRrom1(buffer);
                d = 0;
                aa = String.valueOf(c);
                Log.e("RET1", aa);
            } else if (monitorUV == 2) {
                d = GetFilterValueRrom2(buffer);
                c = 0;
                aa = String.valueOf(d);
                Log.e("RET2", aa);
            } else {
                c = GetFilterValueRrom1(buffer);
                d = GetFilterValueRrom2(buffer);
                aa = String.valueOf(c);
                Log.e("RET12-1", aa);
                aa = String.valueOf(d);
                Log.e("RET12-2", aa);
            }

            int temp;
            if (c > 0) {
                temp = c;
                if (monitorUV != 1)
                    monitorUV1++;
                if (monitorUV1 >= 5) {
                    monitorUV = 1;
                    monitorUV1 = 0;
                    monitorUV2 = 0;
                }
            } else if (d > 0) {
                temp = d;
                if (monitorUV != 2)
                    monitorUV2++;
                if (monitorUV2 >= 5) {
                    monitorUV = 2;
                    monitorUV1 = 0;
                    monitorUV2 = 0;
                }
            } else {
                temp = 0;
            }
            if (temp > 0) {
                if (temback == temp) {
                    autoNum = temback;
                    temback = 0;
                    count = 0;
                } else {
                    temback = temp;
                }
            } else {
                if (count < 3) {
                } else {
                    count = 0;
                    autoNum = 0;
                }
                count++;
            }
            if (listener != null) {
                listener.getAutoNum(autoNum);
            }
            Thread.sleep(100);
        }
    }

    private AutoDataChangeListener listener;

    public interface AutoDataChangeListener {
        void getAutoNum(int autoNum);
    }

    public void setAutoDataChangeListener(AutoDataChangeListener acl) {
        listener = acl;
    }
    // ----------------------------------------------------------------------------------------

    private int GetFilterValueRrom1(byte[] buf1) throws Exception {
        int i, l = 0, f;
        int h1, h2, h4;
        int ret = 0;
        int[] buf = new int[recBufferSize / 2 + 4];

        byte arr[] = new byte[recBufferSize / 4];
        int arrnum;

        byte arr1[] = new byte[51];
        byte arrnum1;

        byte arr2[] = new byte[17];
        byte arrnum2;

        boolean isstart = false;
        int is2or4 = 0;

        String aa = "";

        for (int k = 0; k < recBufferSize; l++, k += 2) {
            buf[l] = buf1[k] + buf1[k + 1] * 256;
            // String bb= String.valueOf(buf[l]);
            // aa=aa+" "+bb;
        }
        // Log.e("---111", aa);

        i = 0;
        f = 0;
        arrnum = 0;
        arrnum1 = 0;
        arrnum2 = 0;

        while (i < recBufferSize / 2 - 2) {
            if (buf[i] >= buf[i + 1]) {
                f++;
            } else {
                if (f != 0) {
                    if (f == 2) {
                        if (arrnum >= 2 && arr[arrnum - 2] == 4
                                && arr[arrnum - 1] == 0)
                            arrnum--;
                        arr[arrnum] = 4;
                    } else if (f >= 3 && f <= 5) {
                        if (arrnum >= 2 && arr[arrnum - 2] == 2
                                && arr[arrnum - 1] == 0)
                            arrnum--;
                        arr[arrnum] = 2;
                    } else if (f >= 6 && f <= 10) {
                        arr[arrnum] = 1;
                    } else {
                        arr[arrnum] = 0;
                    }
                    arrnum++;
                }
                f = 0;
            }
            i++;
        }

        // aa="";
        // aa= String.valueOf(arrnum);
        // for(l=0;l<arrnum;l++)
        // {
        // String bb= String.valueOf(arr[l]);
        // aa=aa+" "+bb;
        // }
        // Log.e("---222", aa);

        i = 0;
        h1 = 0;
        h2 = 0;
        h4 = 0;
        arrnum1 = 0;
        arrnum2 = 0;
        isstart = false;

        while (i < arrnum) {
            if (isstart == false) {
                if (arr[i] == 1)
                    h1++;
                else {
                    if (h1 > 0)
                        h1--;
                }
                if (h1 >= 8)// -----------------------
                {
                    isstart = true;
                    h1 = 0;
                    is2or4 = 0;
                }
                h2 = 0;
                h4 = 0;
            } else {
                if (arr[i] == 2) {
                    if (is2or4 != 2)
                        is2or4 = 2;
                    else
                        is2or4 = 0;
                    h2++;
                    if (h1 > 0)
                        h1--;
                    if (h4 > 0 && is2or4 != 2)
                        h4--;
                } else if (arr[i] == 4) {
                    if (is2or4 != 4)
                        is2or4 = 4;
                    else
                        is2or4 = 0;
                    h4++;
                    if (h1 > 0)
                        h1--;
                    if (h2 > 0 && is2or4 != 4)
                        h2--;
                } else {

                }
                if (is2or4 == 2) {
                    is2or4 = 0;
                    if (h4 >= 4 && h4 <= 8)// ----------------------------
                    {
                        arr1[arrnum1++] = 1;

                        h4 = 0;
                    } else if (h4 >= 10 && h4 <= 15)// ----------------------
                    {
                        arr1[arrnum1++] = 1;

                        arr1[arrnum1++] = 1;

                        h4 = 0;
                    }
                } else if (is2or4 == 4) {
                    is2or4 = 0;
                    if (h2 >= 3 && h2 <= 7)// ------------------------------
                    {
                        arr1[arrnum1++] = 0;

                        h2 = 0;
                    } else if (h2 >= 8 && h2 <= 15)// ----------------------
                    {
                        arr1[arrnum1++] = 0;

                        arr1[arrnum1++] = 0;

                        h2 = 0;
                    }
                }
            }
            if (arrnum1 >= 48) {
                /*
                 * aa=""; aa= String.valueOf((int)arrnum1);
				 * for(l=0;l<arrnum1;l++) { String bb=
				 * String.valueOf((int)arr1[l]); aa=aa+" "+bb; } Log.e("---333",
				 * aa);
				 */
                for (l = 0; l < arrnum1; l += 3) {
                    if (arr1[l] == 1 && arr1[l + 1] == 0 && arr1[l + 2] == 0)
                        arr2[arrnum2++] = 0;
                    if (arr1[l] == 1 && arr1[l + 1] == 1 && arr1[l + 2] == 0)
                        arr2[arrnum2++] = 1;
                }
                if (arrnum2 >= 16) {
					/*
					 * aa=""; aa= String.valueOf((int)arrnum2);
					 * for(l=0;l<arrnum2;l++) { String bb=
					 * String.valueOf((int)arr2[l]); aa=aa+" "+bb; }
					 * Log.e("---444", aa);
					 */
                    if (((arr2[4] + arr2[8] + arr2[12]) % 2) == (arr2[0] + 1) % 2)
                        if (((arr2[5] + arr2[9] + arr2[13]) % 2) == (arr2[1] + 1) % 2)
                            if (((arr2[6] + arr2[10] + arr2[14]) % 2) == (arr2[2] + 1) % 2)
                                if (((arr2[7] + arr2[11] + arr2[15]) % 2) == (arr2[3] + 1) % 2) {
                                    ret = arr2[15] + arr2[14] * 2 + arr2[13]
                                            * 4 + arr2[12] * 8 + arr2[11] * 16
                                            + arr2[10] * 32 + arr2[9] * 64
                                            + arr2[8] * 128 + arr2[7] * 256
                                            + arr2[6] * 512 + arr2[5] * 1024
                                            + arr2[4] * 2048;
                                }
                }
                h1 = 0;
                h2 = 0;
                h4 = 0;
                arrnum1 = 0;
                break;
            }
            i++;
        }// while(i<arrnum)
		/*
		 * aa=""; aa= String.valueOf((int)arrnum1); Log.e("---555", aa);
		 */
        return ret;
    }

    private int GetFilterValueRrom2(byte[] buf1) throws Exception {
        int i, l = 0, f, s;
        int h1, h2, h4;
        int ret = 0;
        int[] buf = new int[recBufferSize / 2 + 4];

        byte arr[] = new byte[recBufferSize / 4];
        int arrnum;

        byte arr1[] = new byte[51];
        byte arrnum1;

        byte arr2[] = new byte[17];
        byte arrnum2;

        boolean isstart = false;
        int is2or4 = 0;

        String aa = "";

        for (int k = 0; k < recBufferSize; l++, k += 2) {
            buf[l] = buf1[k] + buf1[k + 1] * 256;
            // String bb= String.valueOf(buf[l]);
            // aa=aa+" "+bb;
        }
        // Log.e("===111", aa);

        i = 0;
        f = 0;
        s = 0;
        arrnum = 0;
        arrnum1 = 0;
        arrnum2 = 0;

        int ForS = 0;
        while (i < recBufferSize / 2 - 2) {
            if (buf[i] < 0) {
                if (ForS == 2) {
                    if (f > 0 && s > 0) {
                        if (f + s >= 3 && f + s <= 5) {
                            if (arrnum >= 2 && arr[arrnum - 2] == 4
                                    && arr[arrnum - 1] == 0)
                                arrnum--;
                            arr[arrnum] = 4;
                        } else if (f + s >= 7 && f + s <= 9) {
                            if (arrnum >= 2 && arr[arrnum - 2] == 2
                                    && arr[arrnum - 1] == 0)
                                arrnum--;
                            arr[arrnum] = 2;
                        } else if (f + s >= 14 && f + s <= 17) {
                            arr[arrnum] = 1;
                        } else {
                            arr[arrnum] = 0;
                        }
                        arrnum++;
                    }
                    f = 0;
                    s = 0;
                }
                ForS = 1;
                s = 0;
                f++;
            } else {
                ForS = 2;
                s++;
            }
            i++;
        }
		/*
		 * aa=""; aa= String.valueOf(arrnum); for(l=0;l<arrnum;l++) { String bb=
		 * String.valueOf(arr[l]); aa=aa+" "+bb; } Log.e("===222", aa);
		 */

        i = 0;
        h1 = 0;
        h2 = 0;
        h4 = 0;
        arrnum1 = 0;
        arrnum2 = 0;
        isstart = false;

        while (i < arrnum) {
            if (isstart == false) {
                if (arr[i] == 1)
                    h1++;
                else {
                    if (h1 > 0)
                        h1--;
                }
                if (h1 >= 8)// -----------------------
                {
                    isstart = true;
                    h1 = 0;
                    is2or4 = 0;
                }
                h2 = 0;
                h4 = 0;
            } else {
                if (arr[i] == 2) {
                    if (is2or4 != 2)
                        is2or4 = 2;
                    else
                        is2or4 = 0;
                    h2++;
                    if (h1 > 0)
                        h1--;
                    if (h4 > 0 && is2or4 != 2)
                        h4--;
                } else if (arr[i] == 4) {
                    if (is2or4 != 4)
                        is2or4 = 4;
                    else
                        is2or4 = 0;
                    h4++;
                    if (h1 > 0)
                        h1--;
                    if (h2 > 0 && is2or4 != 4)
                        h2--;
                } else {

                }
                if (is2or4 == 2) {
                    is2or4 = 0;
                    if (h4 >= 4 && h4 <= 8)// ----------------------------
                    {
                        arr1[arrnum1++] = 1;

                        h4 = 0;
                    } else if (h4 >= 10 && h4 <= 15)// ----------------------
                    {
                        arr1[arrnum1++] = 1;

                        arr1[arrnum1++] = 1;

                        h4 = 0;
                    }
                } else if (is2or4 == 4) {
                    is2or4 = 0;
                    if (h2 >= 3 && h2 <= 7)// ------------------------------
                    {
                        arr1[arrnum1++] = 0;

                        h2 = 0;
                    } else if (h2 >= 8 && h2 <= 15)// ----------------------
                    {
                        arr1[arrnum1++] = 0;

                        arr1[arrnum1++] = 0;

                        h2 = 0;
                    }
                }
            }
            if (arrnum1 >= 48) {
				/*
				 * aa=""; aa= String.valueOf((int)arrnum1);
				 * for(l=0;l<arrnum1;l++) { String bb=
				 * String.valueOf((int)arr1[l]); aa=aa+" "+bb; } Log.e("===333",
				 * aa);
				 */
                for (l = 0; l < arrnum1; l += 3) {
                    if (arr1[l] == 1 && arr1[l + 1] == 0 && arr1[l + 2] == 0)
                        arr2[arrnum2++] = 0;
                    if (arr1[l] == 1 && arr1[l + 1] == 1 && arr1[l + 2] == 0)
                        arr2[arrnum2++] = 1;
                }
                if (arrnum2 >= 16) {
					/*
					 * aa=""; aa= String.valueOf((int)arrnum2);
					 * for(l=0;l<arrnum2;l++) { String bb=
					 * String.valueOf((int)arr2[l]); aa=aa+" "+bb; }
					 * Log.e("===444", aa);
					 */
                    if (((arr2[4] + arr2[8] + arr2[12]) % 2) == (arr2[0] + 1) % 2)
                        if (((arr2[5] + arr2[9] + arr2[13]) % 2) == (arr2[1] + 1) % 2)
                            if (((arr2[6] + arr2[10] + arr2[14]) % 2) == (arr2[2] + 1) % 2)
                                if (((arr2[7] + arr2[11] + arr2[15]) % 2) == (arr2[3] + 1) % 2) {
                                    ret = arr2[15] + arr2[14] * 2 + arr2[13]
                                            * 4 + arr2[12] * 8 + arr2[11] * 16
                                            + arr2[10] * 32 + arr2[9] * 64
                                            + arr2[8] * 128 + arr2[7] * 256
                                            + arr2[6] * 512 + arr2[5] * 1024
                                            + arr2[4] * 2048;
                                }
                }
                h1 = 0;
                h2 = 0;
                h4 = 0;
                arrnum1 = 0;
                break;
            }
            i++;
        }// while(i<arrnum)
		/*
		 * aa=""; aa= String.valueOf((int)arrnum1); Log.e("===555", aa);
		 */
        return ret;
    }

    public void stopDec() throws Exception {
        stoped = true;
        while (inThread.isAlive()) {
            Thread.sleep(100);
        }
        phoneMIC.stop();
        phoneMIC.release();
        phoneMIC=null;
    }

    public boolean IsStopSta() {
        return stoped;
    }

    public int GetAutoNum() {
        return autoNum;
    }

    // -----------------------------------------------------
}
