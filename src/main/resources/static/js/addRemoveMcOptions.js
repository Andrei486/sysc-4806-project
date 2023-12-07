function addMcOption() {
    const container = document.getElementById("mcOptions");
    const cloneOption = document.getElementById("firstMcOption");
    const clonedOption = cloneOption.cloneNode(true);
    container.append(clonedOption);
}

function removeMcOption() {
    const container = document.getElementById("mcOptions");
    if (container.children.length > 1) {
        container.removeChild(container.lastChild);
    }
}

