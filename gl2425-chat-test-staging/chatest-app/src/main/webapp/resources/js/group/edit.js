document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const groupId = urlParams.get("id");

    if (groupId) {
        fetch(`/api/groups/${groupId}`)
            .then(response => response.json())
            .then(group => {
                document.getElementById("name").value = group.name;
                document.getElementById("description").value = group.description;
            });

        const editForm = document.getElementById("editGroupForm");
        if (editForm) {
            editForm.addEventListener("submit", async function (event) {
                event.preventDefault();
                const name = document.getElementById("name").value;
                const description = document.getElementById("description").value;

                const response = await fetch(`/api/groups/${groupId}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ name, description })
                });

                if (response.ok) {
                    alert("Group updated successfully!");
                    window.location.href = "list.html";
                } else {
                    alert("Failed to update group.");
                }
            });
        }
    }
});
