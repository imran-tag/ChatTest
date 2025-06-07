document.addEventListener("DOMContentLoaded", function () {
    const createForm = document.getElementById("createGroupForm");
    if (createForm) {
        createForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            const name = document.getElementById("name").value;
            const description = document.getElementById("description").value;
            
            const response = await fetch("/api/groups", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ name, description })
            });
            
            if (response.ok) {
                alert("Group created successfully!");
                window.location.href = "list.html";
            } else {
                alert("Failed to create group.");
            }
        });
    }
});
