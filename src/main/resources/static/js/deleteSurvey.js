$(document).ready(function() {
    $("#deleteForm").submit(function(event) {
        // Prevent the default form submission
        event.preventDefault();

        // Reference the form action directly
        let surveyId = $("#deleteForm").attr("action").split('/').pop();

        // Make a DELETE request
        $.ajax({
            url: "/deleteSurvey/" + surveyId,
            type: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken() // Assuming you're using CSRF protection
            },
            error: function(error) {
                // Handle error
                console.error('There was a problem with the AJAX request:', error);
            }
        });
    });

    function getCsrfToken() {
        // Retrieve CSRF token from the page (assuming it's present)
        return document.querySelector('meta[name="_csrf"]').content;
    }
});