document.addEventListener("DOMContentLoaded", function () {
    const groupList = document.getElementById("groupList");
    if (groupList) {
        fetch("/api/groups")
            .then(response => response.json())
            .then(groups => {
                groups.forEach(group => {
                    const li = document.createElement("li");
                    li.innerHTML = `
                        <strong>Name:</strong> ${group.name} <br>
                        <strong>Description:</strong> ${group.description} <br>
                        <a href="edit.html?id=${group.id}">Edit</a>
                        <br><br>
                    `;
                    groupList.appendChild(li);
                });
            });
    }
});
