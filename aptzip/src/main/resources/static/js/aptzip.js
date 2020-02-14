/*
  write - select category
*/
const categoryTypes = document.querySelectorAll('.tt-button-icon')
categoryTypes.forEach(function(input) {
  input.addEventListener('click', function(event) {
    document.querySelector('input[name=category]').value = this.childNodes.item(3).textContent;
  });
});

/*
  write - typing title
*/
const maxInputTopicTitle = 99;
// let inputValue = parseInt(document.getElementsByClassName('tt-value-input').item(0).textContent);
const inputTopicTitle = document.getElementById('inputTopicTitle');

// window.onload는 javascript
// document.ready는 jquery
window.onload = function() {
  document.getElementsByClassName('tt-value-input').item(0).textContent = (maxInputTopicTitle - inputTopicTitle.value.length);
};

// keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
// keypress는 backspace 키를 누를 때 발동하지 않음
// keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
inputTopicTitle.addEventListener('keyup', function() {
  document.getElementsByClassName('tt-value-input').item(0).textContent = (maxInputTopicTitle - inputTopicTitle.value.length);
}, false);