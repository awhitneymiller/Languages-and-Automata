package main

import (
	"log"
	"math/rand"
	"sync"
	"sync/atomic"
	"time"
)

// do simulates doing some work for a random duration based on the input seconds.
func do(seconds int, action ...any) {
	log.Println(action...)
	randomMillis := 500*seconds + rand.Intn(500*seconds)
	time.Sleep(time.Duration(randomMillis) * time.Millisecond)
}

//order represents a customer's order in the restaurant.
type Order struct {
	id         uint64
	customer   string
	reply      chan *Order
	preparedBy string
}

// global atomic counter for order IDs.
var nextOrderID atomic.Uint64

// waiter can hold up to 3 outstanding orders.
var waiter = make(chan *Order, 3)

//  to create a new order with a unique id.
func newOrder(customer string) *Order {
	id := nextOrderID.Add(1)
	return &Order{
		id:       id,
		customer: customer,
		reply:    make(chan *Order),
	}
}

// Cook goroutine: takes orders from waiter, "cooks" them, and sends back on reply.
func cook(name string) {
	log.Println(name, "starting work")
	for {
		order := <-waiter
		do(10, name, "cooking order", order.id, "for", order.customer)
		order.preparedBy = name
		order.reply <- order
	}
}

// Customer goroutine: tries to eat five meals and then goes home.
func customer(name string, wg *sync.WaitGroup) {
	defer wg.Done()

	mealsEaten := 0
	for mealsEaten < 5 {
		order := newOrder(name)
		log.Println(name, "placed order", order.id)

		select {
		case waiter <- order:
			// Order accepted by waiter; wait for meal
			meal := <-order.reply
			// Eat between 1000–2000 ms (do(2, ...) => 1000–2000 ms)
			do(2, name, "eating cooked order", meal.id, "prepared by", meal.preparedBy)
			mealsEaten++
		case <-time.After(7 * time.Second):
			// Waited too long to place order; leave and come back later
			do(5, name, "waiting too long, abandoning order", order.id)
			// do(5, ...) => 2500–5000 ms before trying again
		}
	}

	log.Println(name, "going home")
}

func main() {
	rand.Seed(time.Now().UnixNano())

	customers := []string{
		"Ani", "Bai", "Cat", "Dao", "Eve",
		"Fay", "Gus", "Hua", "Iza", "Jai",
	}

	var wg sync.WaitGroup

	// Start the cooks
	go cook("Remy")
	go cook("Colette")
	go cook("Linguini")

	// starts customers
	wg.Add(len(customers))
	for _, c := range customers {
		name := c
		go customer(name, &wg)
	}

	//waits for customers to go home.
	wg.Wait()

	log.Println("Restaurant closing")
}
