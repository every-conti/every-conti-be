package my.everyconti.every_conti.constant.song;

public enum SongType {
    CCM("CCM"),
    HYMN("찬송가"),
    HYMN_REMAKE("찬송가 리메이크"),
    GOSPEL("복음성가");

    private final String description;

    SongType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
