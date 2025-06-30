$version = '4.9.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'EDC245E7FD6783D494290CA9B8D7F3BC81E95CF156ABA59E2B0148E3E4934524'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
