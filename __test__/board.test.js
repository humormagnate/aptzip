import { keyUpCalcRestTitle } from "../src/main/resources/static/js/aptzip/board.js";

describe("게시물(board)", () => {
  it("제목의 남은 길이를 구한다", () => {
    expect(keyUpCalcRestTitle(4)).toEqual(95);
  });
});
