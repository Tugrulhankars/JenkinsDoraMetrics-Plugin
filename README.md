# Jenkins DORA Metrics Plugin

## Introduction

The **Jenkins DORA Metrics Plugin** is a custom Jenkins plugin designed to track and calculate the four key DORA (DevOps Research and Assessment) metrics for your Jenkins projects. 

By tracking these metrics, software development teams can measure their performance and stability, helping them identify areas for continuous improvement.

## Features

This plugin automatically calculates the following DORA metrics for your jobs:
* **Deployment Frequency (Successful Deployments):** How often code is successfully deployed to production or released to end users.
* **Lead Time for Changes (Mean Lead Time):** How long it takes to go from code committed to code successfully running in production.
* **Change Failure Rate (CFR):** The percentage of deployments causing a failure in production.
* **Time to Restore Service (Mean Time to Recovery - MTTR):** How long it generally takes to restore service when a service incident or a defect that impacts users occurs.

## How It Works

The plugin uses a `RunListener` to intercept completed builds. It evaluates whether the build was successful, measures its duration, and calculates the necessary metrics in real-time. The metrics are then persistently stored in Jenkins and made available on the project's dashboard via a custom action (`DoraMetricsAction`).

## Getting Started

### Prerequisites
* Jenkins `2.426.3` or newer
* Java `11` or newer
* Maven `3.8.x` or newer

### Building the Plugin

To build the plugin locally, run the following Maven command from the project root:

```bash
mvn clean package
```
This will generate a `.hpi` file in the `target/` directory (e.g., `target/JenkinsDoraMetrics.hpi`).

### Installation

1. Go to your Jenkins Dashboard.
2. Navigate to **Manage Jenkins** > **Plugins** > **Advanced settings**.
3. Under the **Deploy Plugin** section, upload the compiled `.hpi` file.
4. Restart Jenkins if required.

## Contributing

Contributions are welcome! If you have suggestions or want to report a bug, please feel free to open an issue or submit a pull request on GitHub.

## License

Licensed under MIT, see [LICENSE.md](LICENSE.md).
