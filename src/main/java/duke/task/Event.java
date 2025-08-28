package duke.task;

import duke.util.DateTimeUtil;
import java.time.LocalDateTime;

public class Event extends Task {
  private final LocalDateTime from;
  private final LocalDateTime to;
  private final boolean fromHasTime;
  private final boolean toHasTime;

  public Event(
      String description,
      LocalDateTime from,
      boolean fromHasTime,
      LocalDateTime to,
      boolean toHasTime) {
    super(description);
    this.from = from;
    this.to = to;
    this.fromHasTime = fromHasTime;
    this.toHasTime = toHasTime;
  }

  public Event(String description, String fromString, String toString) {
    super(description);
    DateTimeUtil.ParseResult fromResult = DateTimeUtil.parseLenientResult(fromString);
    DateTimeUtil.ParseResult toResult = DateTimeUtil.parseLenientResult(toString);

    this.from = fromResult.dt;
    this.to = toResult.dt;
    this.fromHasTime = fromResult.hasTime;
    this.toHasTime = toResult.hasTime;
  }

  public LocalDateTime getFromDateTime() {
    return from;
  }

  public LocalDateTime getToDateTime() {
    return to;
  }

  public String getFrom() {
    return DateTimeUtil.toStorageString(from, fromHasTime);
  }

  public String getTo() {
    return DateTimeUtil.toStorageString(to, toHasTime);
  }

  @Override
  public String toString() {
    String fromStr = DateTimeUtil.toPrettyString(from, fromHasTime);
    String toStr = DateTimeUtil.toPrettyString(to, toHasTime);
    return "[E] ["
        + getStatusIcon()
        + "] "
        + description
        + " (from: "
        + fromStr
        + ", to: "
        + toStr
        + ")";
  }
}
