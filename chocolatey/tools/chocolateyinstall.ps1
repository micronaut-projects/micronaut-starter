$version = '4.10.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '5545AC9B39213B7F8619FA86C45D854EB98AA505299E0D9F82A72817042E96E6'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
