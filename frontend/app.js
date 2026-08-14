const API_URL = "http://localhost:8080";

const loadCardsButton =
    document.getElementById("loadCardsButton");

const cardsContainer =
    document.getElementById("cardsContainer");

const addCardForm =
    document.getElementById("addCardForm");

const recommendationForm =
    document.getElementById("recommendationForm");

const recommendationContainer =
    document.getElementById("recommendationContainer");


loadCardsButton.addEventListener("click", loadCards);

//loadCards function
async function loadCards() {
    const response = await fetch(`${API_URL}/cards`);
    const cards = await response.json();

    cardsContainer.innerHTML = "";

    for (const card of cards) {
        const cardDiv = document.createElement("div");
        cardDiv.classList.add("card");

        cardDiv.innerHTML = `
            <h3>${card.name}</h3>
            <p>Issuer: ${card.issuer}</p>
            <p>Network: ${card.network}</p>
            <p>Annual Fee: $${card.annualFee}</p>

            <button class="toggleRewardsButton">
                View Rewards
            </button>

            <div class="rewardSection hidden">
                <div class="rewardList"></div>

                <h4>Add Reward Rule</h4>

                <form class="rewardForm">
                    <input
                        type="text"
                        class="rewardCategory"
                        placeholder="Category"
                        required
                    >

                    <input
                        type="number"
                        class="rewardCashback"
                        placeholder="Cashback %"
                        min="0.01"
                        step="0.01"
                        required
                    >

                    <button type="submit">
                        Add Reward
                    </button>
                </form>
            </div>
        `;

        cardsContainer.appendChild(cardDiv);

        const toggleButton =
            cardDiv.querySelector(".toggleRewardsButton");

        const rewardSection =
            cardDiv.querySelector(".rewardSection");

        const rewardList =
            cardDiv.querySelector(".rewardList");

        const rewardForm =
            cardDiv.querySelector(".rewardForm");
        // What happens when click on the RewardsButton
        toggleButton.addEventListener("click", async function () {
            rewardSection.classList.toggle("hidden");

            if (!rewardSection.classList.contains("hidden")) {
                await loadRewardRules(card.id, rewardList);
            }
        });
        // What happens when submitting the rewardForm
        rewardForm.addEventListener("submit", (event)=> handleAddReward(event,card,rewardList,rewardForm));
    }
}


//loadRewardRules function
async function loadRewardRules(cardId, rewardList) {
    const response =
        await fetch(
            `${API_URL}/cards/${cardId}/reward-rules`
        );

    const rewards =
        await response.json();

    rewardList.innerHTML = "";

    if (rewards.length === 0) {
        rewardList.innerHTML =
            "<p>No reward rules yet.</p>";

        return;
    }

    for (const reward of rewards) {
        const rewardDiv =
            document.createElement("div");

        rewardDiv.classList.add("reward");

        rewardDiv.innerHTML = `
            <p>
                ${reward.category}:
                ${reward.cashbackPercent}%
            </p>
        `;

        rewardList.appendChild(rewardDiv);
    }
}
//Adding a Card
addCardForm.addEventListener(
    "submit",
    handleAddCard
);

//Recommending a card
recommendationForm.addEventListener(
    "submit",
    handleRecommendation
);


async function handleAddCard(event)
{

        event.preventDefault();

        const cardRequest = {
            name:
                document.getElementById("name").value,

            issuer:
                document.getElementById("issuer").value,

            network:
                document.getElementById("network").value,

            annualFee:
                Number(
                    document.getElementById("annualFee").value
                )
        };

        const response =
            await fetch(`${API_URL}/cards`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(cardRequest)
            });

        if (response.ok) {

            addCardForm.reset();

            await loadCards();

        } else {

            alert("Could not add card.");
        }
    }

// Showing recommendation

async function handleRecommendation(event){
        event.preventDefault();

        const category =
            document.getElementById("category").value;

        const response =
            await fetch(
                `${API_URL}/recommend?category=${encodeURIComponent(category)}`
            );

        const recommendations =
            await response.json();

        recommendationContainer.innerHTML = "";

        if (recommendations.length === 0) {

            recommendationContainer.innerHTML =
                "<p>No recommendation found.</p>";

            return;
        }

        for (const recommendation of recommendations) {

            const recommendationDiv =
                document.createElement("div");

            recommendationDiv.classList.add(
                "recommendation"
            );

            recommendationDiv.innerHTML = `
                <h3>${recommendation.cardName}</h3>
                <p>
                    Cashback:
                    ${recommendation.cashbackPercent}%
                </p>
            `;

            recommendationContainer.appendChild(
                recommendationDiv
            );
        }
}
// Adding a Reward
async function handleAddReward (event, card, rewardList, rewardForm)
{
            event.preventDefault();

            const category =
                rewardForm.querySelector(".rewardCategory").value;

            const cashbackPercent =
                Number(
                    rewardForm.querySelector(".rewardCashback").value
                );

            const rewardRequest = {
                category: category,
                cashbackPercent: cashbackPercent
            };

            const response =
                await fetch(
                    `${API_URL}/cards/${card.id}/reward-rules`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify(rewardRequest)
                    }
                );

            if (response.ok) {
                rewardForm.reset();

                await loadRewardRules(
                    card.id,
                    rewardList
                );
            } else {
                alert("Could not add reward rule.");
            }
        }