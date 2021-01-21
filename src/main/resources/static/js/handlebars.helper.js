function makeTemplate(source) {
  return Handlebars.compile(source);
}

function hbs(sourceId, data, container, USER_ID) {
  const source = document.getElementById(sourceId).innerHTML;
  const template = makeTemplate(source);
  // console.log(source);
  // console.log(container);
  //   console.log(data);
  //   console.log(USER_ID);
  const html = template({ list: data, login: USER_ID });
  // console.log("html >> ", html);

  container.innerHTML = html;
}

Handlebars.registerHelper('subString', function (passedString) {
  var theString = passedString.substring(0, 1);
  return new Handlebars.SafeString(theString)
});

// https://stackoverflow.com/questions/8853396/logical-operator-in-a-handlebars-js-if-conditional
Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
  switch (operator) {
    case '==':
      return (v1 == v2) ? options.fn(this) : options.inverse(this);
    case '===':
      return (v1 === v2) ? options.fn(this) : options.inverse(this);
    case '!=':
      return (v1 != v2) ? options.fn(this) : options.inverse(this);
    case '!==':
      return (v1 !== v2) ? options.fn(this) : options.inverse(this);
    case '<':
      return (v1 < v2) ? options.fn(this) : options.inverse(this);
    case '<=':
      return (v1 <= v2) ? options.fn(this) : options.inverse(this);
    case '>':
      return (v1 > v2) ? options.fn(this) : options.inverse(this);
    case '>=':
      return (v1 >= v2) ? options.fn(this) : options.inverse(this);
    case '&&':
      return (v1 && v2) ? options.fn(this) : options.inverse(this);
    case '||':
      return (v1 || v2) ? options.fn(this) : options.inverse(this);
    default:
      return options.inverse(this);
  }
});

/**
 * https://momentjs.com/
 */
Handlebars.registerHelper('moment', function (time) {
  return moment(time).format('YYYY[-]MM[-]DD');
});