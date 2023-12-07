function deleteSurvey(surveyId) {
    // Logic to handle the delete operation
    console.log('Deleting survey with ID:', surveyId);

    // Make a DELETE request
    $.ajax({
        url: "/deleteSurvey/" + surveyId,
        type: "DELETE",
        headers: {
            'Content-Type': 'application/json',
        },
        error: function(error) {
            // Handle error
            console.error('There was a problem with the AJAX request:', error);
        }
    });
}

$(document).ready(function() {
    $("#deleteForm").submit(function(event) {
        // Prevent the default form submission
        event.preventDefault();

        // Reference the form action directly
        let surveyId = $("#deleteForm").attr("action").split('/').pop();

        // Call the global deleteSurvey function
        deleteSurvey(surveyId);
    });
});