$version = '4.1.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '1B33CFF58771BC9F7E015CA0480AC8D853D30B8C92CEFFCCD38AB09FA96EAC72'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
