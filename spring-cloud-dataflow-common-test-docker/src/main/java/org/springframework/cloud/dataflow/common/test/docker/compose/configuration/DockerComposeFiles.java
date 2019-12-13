/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.dataflow.common.test.docker.compose.configuration;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

public class DockerComposeFiles {

	private final List<File> dockerComposeFiles;

	public DockerComposeFiles(List<File> dockerComposeFiles) {
		this.dockerComposeFiles = dockerComposeFiles;
	}

	public static DockerComposeFiles from(String... dockerComposeFilenames) {
		List<File> dockerComposeFiles = Arrays.asList(dockerComposeFilenames).stream()
				.map(File::new)
				.collect(toList());
		validateAtLeastOneComposeFileSpecified(dockerComposeFiles);
		validateComposeFilesExist(dockerComposeFiles);
		return new DockerComposeFiles(dockerComposeFiles);
	}

	public List<String> constructComposeFileCommand() {
		return dockerComposeFiles.stream()
				.map(File::getAbsolutePath)
				.map(f -> Arrays.asList("--file", f))
				.flatMap(Collection::stream)
				.collect(toList());
	}

	private static void validateAtLeastOneComposeFileSpecified(List<File> dockerComposeFiles) {
		Assert.state(!dockerComposeFiles.isEmpty(), "A docker compose file must be specified.");
	}

	private static void validateComposeFilesExist(List<File> dockerComposeFiles) {
		List<File> missingFiles = dockerComposeFiles.stream()
													.filter(f -> !f.exists())
													.collect(toList());

		String errorMessage = missingFiles.stream()
				.map(File::getAbsolutePath)
				.collect(joining(", ", "The following docker-compose files: ", " do not exist."));
		Assert.state(missingFiles.isEmpty(), errorMessage);
	}

}
