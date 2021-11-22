package ir.co.sadad.paymentBill.enums;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum ChargeStatus {

    INIT(1, "INIT", "ثبت اولیه"),
    VERIFYTOPUPDONE(2, "VERIFYTOPUPDONE", "بررسی شارژ مستقیم موفق"),
    VERIFYTOPUPPEND(3, "VERIFYTOPUPPEND", "بررسی شارژ مستقیم معلق"),
    VERIFYTOPUPFAILED(4, "VERIFYTOPUPFAILED", "بررسی شارژ مستقیم ناموفق"),
    PAYMENTDONE(5, "PAYMENTDONE", "پرداخت موفق"),
    PAYMENTPEND(6, "PAYMENTPEND", "پرداخت معلق"),
    PAYMENTFAILED(7, "PAYMENTFAILED", "پرداخت ناموفق"),
    TOPUPDONE(8, "TOPUPDONE", "شارژ موفق"),
    TOPUPPEND(9, "TOPUPPEND", "شارژ معلق"),
    TOPUPFAILED(10, "TOPUPFAILED", "شارژ ناموفق"),
    GPRSDONE(11, "GPRSDONE", "شارژ موفق"),
    GPRSPEND(12, "GPRSPEND", "شارژ معلق"),
    GPRSFAILED(13, "GPRSFAILED", "شارژ ناموفق"),
    INDIRECTDONE(14, "INDIRECTDONE", "شارژ موفق"),
    INDIRECTPEND(15, "INDIRECTPEND", "شارژ معلق"),
    INDIRECTFAILED(16, "INDIRECTFAILED", "شارژ ناموفق");

    public final int Id;
    public final String Name;
    public final String DisplayName;

    ChargeStatus(int id, String name, String displayName) {
        Id = id;
        Name = name;
        DisplayName = displayName;
    }

    private static final Map<Integer, ChargeStatus> _map = new HashMap<Integer, ChargeStatus>();
    private static final Map<ChargeStatus, Integer> _map1 = new HashMap<ChargeStatus, Integer>();

    static {
        for (ChargeStatus trnsTyp : ChargeStatus.values()) {
            _map.put(trnsTyp.Id, trnsTyp);
            _map1.put(trnsTyp, trnsTyp.Id);
        }
    }

    public static int GetId(ChargeStatus typ) {
        return _map1.get(typ);
    }

    public static ChargeStatus GetChargeStatus(int id) {
        return _map.get(id);
    }

    public static Collection<ChargeStatus> GetAll() {
        return _map.values();
    }
}
