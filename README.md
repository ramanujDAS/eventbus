## eventbus
#Overview
The EventBus is a lightweight and efficient message-passing system designed to facilitate communication between components or services in a decoupled architecture. This implementation supports advanced features such as idempotency, concurrent event handling, push/pull models, retry on failure, and dead letter queue (DLQ) to ensure robust and reliable communication.

Features
Idempotency
The EventBus ensures that duplicate events are ignored to prevent redundant processing. Each event is assigned a unique identifier, and processed event IDs are tracked to avoid re-processing.

Concurrent Event Handling
Multiple events can be processed concurrently, improving throughput and scalability. Consumers can spawn multiple workers to handle events in parallel while ensuring thread-safety.

Push/Pull Models

Push Model: Events are pushed to subscribers as soon as they are published.
Pull Model: Subscribers can poll for events when ready, ensuring greater control over processing.
Retry on Failure
When an event fails to be processed by a consumer, the EventBus retries delivery a configurable number of times with an exponential backoff strategy.

Dead Letter Queue (DLQ)
Failed events that exceed the maximum retry count are moved to a Dead Letter Queue (DLQ) for later analysis or manual intervention.


