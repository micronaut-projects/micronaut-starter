$version = '3.4.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F3179CBD437BB0C44BBD12B6ABA5A8A9E110F977FDE4D93F54CF65BEFB3EBA7B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
