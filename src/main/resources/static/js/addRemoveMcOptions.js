let container;
let cloneOption;

document.addEventListener("DOMContentLoaded", function () {
    container = document.getElementById("mcOptions");
    cloneOption = document.getElementById("firstMcOption");
});


function addMcOption() {
    const clonedOption = cloneOption.cloneNode(true);
    container.append(clonedOption);
}

