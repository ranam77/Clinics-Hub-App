package com.ranamustafa.clinicshub.data.model;

public class SlotListItem {
    public static final int TYPE_DAY = 0;
    public static final int TYPE_SLOT = 1;
    private final int type;
    private String day;
    private final SlotModel slot;
    public SlotListItem(int type, String day, SlotModel slot) {
        this.type = type;
        this.day = day;
        this.slot = slot;
    }
    public static SlotListItem slot(SlotModel slot) {
        return new SlotListItem(TYPE_SLOT, null, slot);
    }
    public SlotListItem daytoslot(String day) {
        return new SlotListItem(TYPE_DAY, day, (SlotModel) null);
    }
    public int getType() {
        return type;
    }

    public String getDay() {
        return day;
    }
    public SlotModel getSlot() {
        return slot;
    }
    public void setDay(String day)
    {
        this.day = day;
    }
}
