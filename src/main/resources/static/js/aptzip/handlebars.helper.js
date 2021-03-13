// deprecated
// <script src="https://momentjs.com/downloads/moment-with-locales.min.js"></script>
// <script src="https://cdn.jsdelivr.net/npm/handlebars@4.7.3/dist/handlebars.js"></script>

function makeTemplate(source) {
  return Handlebars.compile(source);
}

function hbs(sourceId, data, container, USER_ID) {
  const source = document.getElementById(sourceId).innerHTML;
  const template = makeTemplate(source);
  const html = template({ list: data, login: USER_ID });
  container.innerHTML = html;
}

Handlebars.registerHelper("subString", function (passedString) {
  var theString = passedString.substring(0, 1);
  return new Handlebars.SafeString(theString);
});

Handlebars.registerHelper("ifCond", function (v1, operator, v2, options) {
  switch (operator) {
    case "==":
      return v1 == v2 ? options.fn(this) : options.inverse(this);
    case "===":
      return v1 === v2 ? options.fn(this) : options.inverse(this);
    case "!=":
      return v1 != v2 ? options.fn(this) : options.inverse(this);
    case "!==":
      return v1 !== v2 ? options.fn(this) : options.inverse(this);
    case "<":
      return v1 < v2 ? options.fn(this) : options.inverse(this);
    case "<=":
      return v1 <= v2 ? options.fn(this) : options.inverse(this);
    case ">":
      return v1 > v2 ? options.fn(this) : options.inverse(this);
    case ">=":
      return v1 >= v2 ? options.fn(this) : options.inverse(this);
    case "&&":
      return v1 && v2 ? options.fn(this) : options.inverse(this);
    case "||":
      return v1 || v2 ? options.fn(this) : options.inverse(this);
    default:
      return options.inverse(this);
  }
});

Handlebars.registerHelper("moment", function (time) {
  return moment(time).format("YYYY[-]MM[-]DD");
});
