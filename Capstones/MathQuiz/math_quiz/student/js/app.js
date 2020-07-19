let gameOver = false;
function getRandomNumber(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

function shuffleArray(arr) {
    return arr.sort(function (a, b) { return Math.random() - 0.5 })
}

function generateProblems(numProblems = 10) {
    let problems = [];
    for (let i = 0; i < numProblems; i++) {
        let problem = {
            leftOperand: getRandomNumber(10),
            rightOperand: getRandomNumber(10),
        }
        problems.push(problem)
    }
    // console.log(problems)
    // console.log(problems[2].operandOne);
    return problems;
}

function generateSolutions(problem) {
    let anArray = []
    anArray.push(problem.leftOperand * problem.rightOperand);
    while (anArray.length < 4) {
        let newNum = getRandomNumber(82);
        if (!anArray.includes(newNum)) {
            anArray.push(newNum)
        }
    }
    return shuffleArray(anArray);
}

function displayProblem(problem) {
    let problemStr = problem.leftOperand + " * " + problem.rightOperand;
    let problemElement = document.querySelector('div.expression');
    problemElement.innerText = problemStr;
}

function displayAnswers(anArray) {
    const listItems = document.querySelector('ul').querySelectorAll('li');
    for (let i = 0; i < listItems.length; i++) {
        listItems[i].innerText = anArray[i];

    }

}

function updateScore(num) {
    let scoreNumDisplay = document.querySelector('span.currentScore');
    scoreNumDisplay.innerText = num;

}

function updateProblemNum(num) {
    let problemNumDisplay = document.querySelector('span.currentProblem');
    problemNumDisplay.innerText = num;
}



document.addEventListener('DOMContentLoaded', () => {
    const listItems = document.querySelector('ul').querySelectorAll('li');
    const btn = document.getElementById('btnStartOver');
    let activeProblem = 0;
    let problemsArray = generateProblems();
    let score = 0;
    // console.log(problemsArray.length);
    // console.log(activeProblem);
    displayProblem(problemsArray[activeProblem]);
    displayAnswers(generateSolutions(problemsArray[activeProblem]));

    btn.addEventListener('click', (event) => {
        activeProblem = 0;
        problemsArray = generateProblems();
        score = 0;
        displayProblem(problemsArray[activeProblem]);
        displayAnswers(generateSolutions(problemsArray[activeProblem]));
        updateScore(0);
        updateProblemNum(1);
        let elementsToUnhide = document.querySelectorAll('.show-hide');
        // console.log(elementsToUnhide.length);
        elementsToUnhide.forEach(element => {
            element.classList.remove('hidden');
        });
    });
    listItems.forEach(choice => {

        choice.addEventListener('click', (event) => {
            let solution = problemsArray[activeProblem].leftOperand * problemsArray[activeProblem].rightOperand;
            let selectedChoice = choice.innerText;
            if (selectedChoice == solution) {
                score += 1;
                updateScore(score);
            }
            // console.log(selectedChoice);
            // console.log(solution);
            if (activeProblem < 9) {
                activeProblem += 1;
                // console.log(activeProblem);
                displayProblem(problemsArray[activeProblem]);
                displayAnswers(generateSolutions(problemsArray[activeProblem]));
                updateProblemNum(activeProblem + 1);

            } else {

                let elementsToHide = document.querySelectorAll('.show-hide');
                // console.log(elementsToHide.length);
                elementsToHide.forEach(element => {
                    element.classList.add('hidden');
                });

            }
        })

    });


})
