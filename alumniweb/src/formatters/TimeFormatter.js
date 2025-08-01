import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import "dayjs/locale/vi";

dayjs.extend(relativeTime);
dayjs.locale("vi");

export function formatTimeVi(createdAt) {
  const now = dayjs();
  const date = dayjs(createdAt);

  if (now.isSame(date, "day")) {
    return date.fromNow();
  }

  if (now.subtract(1, "day").isSame(date, "day")) {
    return `Hôm qua lúc ${date.format("HH:mm")}`;
  }

  if (now.diff(date, "day") < 6) {
    return `${date.format("dddd")} lúc ${date.format("HH:mm")}`;
  }

  return date.format("DD/MM/YYYY [lúc] HH:mm");
}
