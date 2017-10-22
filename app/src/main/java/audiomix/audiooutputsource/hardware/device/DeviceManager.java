package audiomix.audiooutputsource.hardware.device;

/**
 * Created by dylan on 03-Aug-17.
 */

public  class DeviceManager {
    private final String TAG = "CON_DeviceManager";
    public static final int DEVICE_NONE = 0x0;
    // reserved bits
    public static final int DEVICE_BIT_IN = 0x80000000;
    public static final int DEVICE_BIT_DEFAULT = 0x40000000;
    // output devices, be sure to update AudioManager.java also
    public static final int DEVICE_OUT_EARPIECE = 0x1;
    public static final int DEVICE_OUT_SPEAKER = 0x2;
    public static final int DEVICE_OUT_WIRED_HEADSET = 0x4;
    public static final int DEVICE_OUT_WIRED_HEADPHONE = 0x8;
    public static final int DEVICE_OUT_BLUETOOTH_SCO = 0x10;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_HEADSET = 0x20;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_CARKIT = 0x40;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP = 0x80;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES = 0x100;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER = 0x200;
    public static final int DEVICE_OUT_AUX_DIGITAL = 0x400;
    public static final int DEVICE_OUT_HDMI = DEVICE_OUT_AUX_DIGITAL;
    public static final int DEVICE_OUT_ANLG_DOCK_HEADSET = 0x800;
    public static final int DEVICE_OUT_DGTL_DOCK_HEADSET = 0x1000;
    public static final int DEVICE_OUT_USB_ACCESSORY = 0x2000;
    public static final int DEVICE_OUT_USB_DEVICE = 0x4000;
    public static final int DEVICE_OUT_REMOTE_SUBMIX = 0x8000;
    public static final int DEVICE_OUT_TELEPHONY_TX = 0x10000;
    public static final int DEVICE_OUT_LINE = 0x20000;
    public static final int DEVICE_OUT_HDMI_ARC = 0x40000;
    public static final int DEVICE_OUT_SPDIF = 0x80000;
    public static final int DEVICE_OUT_FM = 0x100000;
    public static final int DEVICE_OUT_AUX_LINE = 0x200000;
    public static final int DEVICE_OUT_SPEAKER_SAFE = 0x400000;
    public static final int DEVICE_OUT_IP = 0x800000;
    public static final int DEVICE_OUT_BUS = 0x1000000;
    public static final int DEVICE_OUT_PROXY = 0x2000000;
    public static final int DEVICE_OUT_USB_HEADSET = 0x4000000;

    public static final int DEVICE_OUT_DEFAULT = DEVICE_BIT_DEFAULT;

    public static final int DEVICE_OUT_ALL = (DEVICE_OUT_EARPIECE |
            DEVICE_OUT_SPEAKER |
            DEVICE_OUT_WIRED_HEADSET |
            DEVICE_OUT_WIRED_HEADPHONE |
            DEVICE_OUT_BLUETOOTH_SCO |
            DEVICE_OUT_BLUETOOTH_SCO_HEADSET |
            DEVICE_OUT_BLUETOOTH_SCO_CARKIT |
            DEVICE_OUT_BLUETOOTH_A2DP |
            DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES |
            DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER |
            DEVICE_OUT_HDMI |
            DEVICE_OUT_ANLG_DOCK_HEADSET |
            DEVICE_OUT_DGTL_DOCK_HEADSET |
            DEVICE_OUT_USB_ACCESSORY |
            DEVICE_OUT_USB_DEVICE |
            DEVICE_OUT_REMOTE_SUBMIX |
            DEVICE_OUT_TELEPHONY_TX |
            DEVICE_OUT_LINE |
            DEVICE_OUT_HDMI_ARC |
            DEVICE_OUT_SPDIF |
            DEVICE_OUT_FM |
            DEVICE_OUT_AUX_LINE |
            DEVICE_OUT_SPEAKER_SAFE |
            DEVICE_OUT_IP |
            DEVICE_OUT_BUS |
            DEVICE_OUT_PROXY |
            DEVICE_OUT_USB_HEADSET |
            DEVICE_OUT_DEFAULT);
    /*device name*/
    public static final String DEVICE_OUT_EARPIECE_NAME = "earpiece";
    public static final String DEVICE_OUT_SPEAKER_NAME = "speaker";
    public static final String DEVICE_OUT_WIRED_HEADSET_NAME = "headset";
    public static final String DEVICE_OUT_WIRED_HEADPHONE_NAME = "headphone";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_NAME = "bt_sco";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME = "bt_sco_hs";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME = "bt_sco_carkit";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_NAME = "bt_a2dp";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME = "bt_a2dp_hp";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME = "bt_a2dp_spk";
    public static final String DEVICE_OUT_AUX_DIGITAL_NAME = "aux_digital";
    public static final String DEVICE_OUT_HDMI_NAME = "hdmi";
    public static final String DEVICE_OUT_ANLG_DOCK_HEADSET_NAME = "analog_dock";
    public static final String DEVICE_OUT_DGTL_DOCK_HEADSET_NAME = "digital_dock";
    public static final String DEVICE_OUT_USB_ACCESSORY_NAME = "usb_accessory";
    public static final String DEVICE_OUT_USB_DEVICE_NAME = "usb_device";
    public static final String DEVICE_OUT_REMOTE_SUBMIX_NAME = "remote_submix";
    public static final String DEVICE_OUT_TELEPHONY_TX_NAME = "telephony_tx";
    public static final String DEVICE_OUT_LINE_NAME = "line";
    public static final String DEVICE_OUT_HDMI_ARC_NAME = "hmdi_arc";
    public static final String DEVICE_OUT_SPDIF_NAME = "spdif";
    public static final String DEVICE_OUT_FM_NAME = "fm_transmitter";
    public static final String DEVICE_OUT_AUX_LINE_NAME = "aux_line";
    public static final String DEVICE_OUT_SPEAKER_SAFE_NAME = "speaker_safe";
    public static final String DEVICE_OUT_IP_NAME = "ip";
    public static final String DEVICE_OUT_BUS_NAME = "bus";
    /*device state*/
    public static final int DEVICE_STATE_UNAVAILABLE   = 0;
    public static final int DEVICE_STATE_AVAILABLE     = 1;

    public String getDeviceName(int deviceId) {
        switch(deviceId) {
            case DEVICE_OUT_EARPIECE:
                return DEVICE_OUT_EARPIECE_NAME;
            case DEVICE_OUT_SPEAKER:
                return DEVICE_OUT_SPEAKER_NAME;
            case DEVICE_OUT_WIRED_HEADSET:
                return DEVICE_OUT_WIRED_HEADSET_NAME;
            case DEVICE_OUT_WIRED_HEADPHONE:
                return DEVICE_OUT_WIRED_HEADPHONE_NAME;
            case DEVICE_OUT_BLUETOOTH_SCO:
                return DEVICE_OUT_BLUETOOTH_SCO_NAME;
            case DEVICE_OUT_BLUETOOTH_SCO_HEADSET:
                return DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME;
            case DEVICE_OUT_BLUETOOTH_SCO_CARKIT:
                return DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME;
            case DEVICE_OUT_BLUETOOTH_A2DP:
                return DEVICE_OUT_BLUETOOTH_A2DP_NAME;
            case DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES:
                return DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME;
            case DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER:
                return DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME;
            case DEVICE_OUT_HDMI:
                return DEVICE_OUT_HDMI_NAME;
            case DEVICE_OUT_ANLG_DOCK_HEADSET:
                return DEVICE_OUT_ANLG_DOCK_HEADSET_NAME;
            case DEVICE_OUT_DGTL_DOCK_HEADSET:
                return DEVICE_OUT_DGTL_DOCK_HEADSET_NAME;
            case DEVICE_OUT_USB_ACCESSORY:
                return DEVICE_OUT_USB_ACCESSORY_NAME;
            case DEVICE_OUT_USB_DEVICE:
                return DEVICE_OUT_USB_DEVICE_NAME;
            case DEVICE_OUT_REMOTE_SUBMIX:
                return DEVICE_OUT_REMOTE_SUBMIX_NAME;
            case DEVICE_OUT_TELEPHONY_TX:
                return DEVICE_OUT_TELEPHONY_TX_NAME;
            case DEVICE_OUT_LINE:
                return DEVICE_OUT_LINE_NAME;
            case DEVICE_OUT_HDMI_ARC:
                return DEVICE_OUT_HDMI_ARC_NAME;
            case DEVICE_OUT_SPDIF:
                return DEVICE_OUT_SPDIF_NAME;
            case DEVICE_OUT_FM:
                return DEVICE_OUT_FM_NAME;
            case DEVICE_OUT_AUX_LINE:
                return DEVICE_OUT_AUX_LINE_NAME;
            case DEVICE_OUT_SPEAKER_SAFE:
                return DEVICE_OUT_SPEAKER_SAFE_NAME;
            case DEVICE_OUT_IP:
                return DEVICE_OUT_IP_NAME;
            case DEVICE_OUT_BUS:
                return DEVICE_OUT_BUS_NAME;
            case DEVICE_OUT_DEFAULT:
            default:
                return Integer.toString(deviceId);
        }
    }





}
